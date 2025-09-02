package project.OnlineTrainingProgram.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.OnlineTrainingProgram.Dao.UserDAO;
import project.OnlineTrainingProgram.Model.UserModel;
import java.util.List;

@Service
public class UserService {

	@Autowired
    private UserDAO userDAO;
    public void saveUser(UserModel userModel) {
        userDAO.save(userModel);
    }

    public UserModel getUserById(int userId) {
        return userDAO.getUserById(userId);
    }

    public List<UserModel> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public void deleteUser(int id) {
        userDAO.delete(id);
    }
    public UserModel getUserByEmail(String email) {
    	return userDAO.getUserByEmail(email);
    }
    public UserModel authenticate(String email, String password) {
        return userDAO.authenticate(email, password);
    }
}
