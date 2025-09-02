package project.OnlineTrainingProgram.UI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.OnlineTrainingProgram.Model.UserModel;
import project.OnlineTrainingProgram.Service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserAPI {

    @Autowired
    private UserService userService;

    @PostMapping
    public String saveUser(@RequestBody UserModel user) {
        userService.saveUser(user);
        return "User saved successfully!";
    }

    
    @GetMapping("/{id}")
    public UserModel getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

  
    @GetMapping
    public List<UserModel> getAllUsers() {
        return userService.getAllUsers();
    }

   
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return "User deleted successfully!";
    }
}
