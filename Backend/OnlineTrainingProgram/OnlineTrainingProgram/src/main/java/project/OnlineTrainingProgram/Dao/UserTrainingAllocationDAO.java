package project.OnlineTrainingProgram.Dao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import project.OnlineTrainingProgram.Entity.ProgramAllocation;
import project.OnlineTrainingProgram.Entity.TrainingProgram;
import project.OnlineTrainingProgram.Entity.User;
import project.OnlineTrainingProgram.Model.UserTrainingAllocationModel;

@Repository
@Transactional
public class UserTrainingAllocationDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(UserTrainingAllocationModel model) {
        ProgramAllocation entity;
        if (model.getAllocationId() == 0) {
            entity = toEntity(model);
            entityManager.persist(entity);
        } else {
            entity = entityManager.find(ProgramAllocation.class, model.getAllocationId());
            if (entity != null) {
                entity.setAllocationDate(model.getAllocationDate());
                entity.setUser(entityManager.find(User.class,model.getUserId()));
                entity.setProgram(entityManager.find(TrainingProgram.class, (model.getProgramId())));
                entity.setAllocatedBy(entityManager.find(User.class, (model.getAllocatedById())));
                entityManager.merge(entity);
            }

        }
    }
    public ProgramAllocation getByUserIdAndTrainingId(int userId, int trainingId) {
        return entityManager.createQuery(
                "SELECT a FROM ProgramAllocation a WHERE a.user.userId = :userId AND a.program.programId = :trainingId",
                ProgramAllocation.class)
            .setParameter("userId", userId)
            .setParameter("trainingId", trainingId)
            .getResultStream()
            .findFirst()
            .orElse(null);
    }


    public UserTrainingAllocationModel getAllocationById(int id) {
        ProgramAllocation entity = entityManager.find(ProgramAllocation.class, id);
        return entity != null ? toModel(entity) : null;
    }

    public List<UserTrainingAllocationModel> getAllAllocations() {
        List<ProgramAllocation> allocations =
                entityManager.createQuery("SELECT a FROM ProgramAllocation a", ProgramAllocation.class).getResultList();
        return allocations.stream().map(this::toModel).collect(Collectors.toList());
    }

    public void delete(int id) {
        ProgramAllocation allocation = entityManager.find(ProgramAllocation.class, id);
        if (allocation != null) {
            entityManager.remove(allocation);
        }
    }
    
    public List<ProgramAllocation> getAllocationsByUserId(int userId) {
        return entityManager.createQuery(
                "SELECT a FROM ProgramAllocation a WHERE a.user.userId = :userId",
                ProgramAllocation.class)
            .setParameter("userId", userId)
            .getResultList();
    }
    // Save ProgramAllocation entity directly
    public void saveEntity(ProgramAllocation allocation) {
        entityManager.persist(allocation);
    }


    // Conversion methods
    private UserTrainingAllocationModel toModel(ProgramAllocation entity) {
        UserTrainingAllocationModel model = new UserTrainingAllocationModel();
        model.setAllocationId(entity.getAllocationId());
        model.setUserId(entity.getUser().getUserId());
        model.setProgramId(entity.getProgram().getProgramId());
        model.setAllocatedById(entity.getAllocatedBy() != null ? entity.getAllocatedBy().getUserId() : 0);
        model.setAllocationDate(entity.getAllocationDate());
        return model;
    }

    private ProgramAllocation toEntity(UserTrainingAllocationModel model) {
        ProgramAllocation entity = new ProgramAllocation();
        entity.setAllocationId(model.getAllocationId());
        entity.setAllocationDate(model.getAllocationDate());
        entity.setUser(entityManager.find(User.class, model.getUserId()));
        entity.setProgram(entityManager.find(TrainingProgram.class, model.getProgramId()));
        entity.setAllocatedBy(entityManager.find(User.class, model.getAllocatedById()));
        return entity;
    }
}
