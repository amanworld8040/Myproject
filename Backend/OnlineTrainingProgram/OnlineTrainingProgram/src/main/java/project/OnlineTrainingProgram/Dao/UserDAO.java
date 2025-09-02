package project.OnlineTrainingProgram.Dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import project.OnlineTrainingProgram.Entity.User;
import project.OnlineTrainingProgram.Entity.User.Role;
import project.OnlineTrainingProgram.Model.UserModel;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
@Repository
@Transactional
public class UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    // Save or update
    public void save(UserModel model) {
        User entity;
        if (model.getUserId() == 0) {
            entity = new User();
        } else {
            entity = entityManager.find(User.class, model.getUserId());
            if (entity == null) entity = new User();
        }

        entity.setName(model.getName());
        entity.setEmail(model.getEmail());
        entity.setPhoneNo(model.getPhoneNo());
        entity.setPassword(model.getPassword()); // critical
        if (model.getRole() != null) {
            entity.setRole(Role.valueOf(model.getRole().toUpperCase()));
        }

        if (model.getUserId() == 0) {
            entityManager.persist(entity);
            model.setUserId(entity.getUserId());
        } else {
            entityManager.merge(entity);
        }
    }

    public UserModel getUserById(int id) {
        User entity = entityManager.find(User.class, id);
        return entity != null ? toModel(entity) : null;
    }

    public List<UserModel> getAllUsers() {
        List<User> users = entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
        return users.stream().map(this::toModel).collect(Collectors.toList());
    }

    public void delete(int id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }
    public UserModel getUserByEmail(String email) {
        try {
            User user = entityManager
                .createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
            return toModel(user);
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public UserModel authenticate(String email, String rawPassword) {
        List<User> found = entityManager
                .createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .setMaxResults(1)
                .getResultList();

        if (found.isEmpty()) return null;

        User u = found.get(0);
        // For now plain comparison. If you hash, compare hashed values here.
        if (u.getPassword() != null && u.getPassword().equals(rawPassword)) {
            return toModel(u); // do not set password in the model we return
        }
        return null;
    }

    private UserModel toModel(User entity) {
        UserModel model = new UserModel();
        model.setUserId(entity.getUserId());
        model.setName(entity.getName());
        model.setEmail(entity.getEmail());
        model.setPhoneNo(entity.getPhoneNo());
        model.setPassword(entity.getPassword());
        model.setRole(entity.getRole().name());
        model.setProgramCount(entity.getProgramAllocations() != null ? entity.getProgramAllocations().size() : 0);
        return model;
    }
}
