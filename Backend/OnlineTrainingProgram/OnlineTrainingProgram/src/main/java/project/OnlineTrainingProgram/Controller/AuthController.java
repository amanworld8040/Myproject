package project.OnlineTrainingProgram.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import project.OnlineTrainingProgram.Model.UserModel;
import project.OnlineTrainingProgram.Service.UserService;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/api/signup")
    public ResponseEntity<?> signup(@RequestBody(required = false) Map<String, String> body) {
        String name = body.getOrDefault("name", "").trim();
        String email = body.getOrDefault("email", "").toLowerCase().trim();
        String phone = body.getOrDefault("phone", "").trim();
        String password = body.getOrDefault("password", "").trim();
        String role = body.getOrDefault("role", "").toUpperCase().trim();

        if (email.isBlank() || password.isBlank() || name.isBlank() || phone.isBlank() || role.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "All fields are required"
            ));
        }

        if (!role.equals("USER") && !role.equals("ADMIN")) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Role must be USER or ADMIN"
            ));
        }

        // check existing in DB via service
        UserModel existing = userService.getUserByEmail(email);
        if (existing != null) {
            return ResponseEntity.status(409).body(Map.of(
                "success", false,
                "message", "Email already exists"
            ));
        }

        // create and save user
        UserModel user = new UserModel();
        user.setName(name);
        user.setEmail(email);
        user.setPhoneNo(phone);
        user.setPassword(password);
        user.setRole(role);
        userService.saveUser(user);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "user", Map.of("email", email, "name", name, "role", role),
            "token", "demo-token"
        ));
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody(required = false) Map<String, String> body) {
        if (body == null) body = new HashMap<>();
        String email = body.getOrDefault("email", "").toLowerCase().trim();
        String password = body.getOrDefault("password", "").trim();

        if (email.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Email and password are required"
            ));
        }

        // prefer authenticate if implemented
        UserModel authenticated = userService.authenticate(email, password);
        if (authenticated == null) {
            // fallback: try fetch by email and compare password
            UserModel stored = userService.getUserByEmail(email);
            if (stored == null || !password.equals(stored.getPassword())) {
                return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Invalid credentials"
                ));
            }
            authenticated = stored;
        }

        return ResponseEntity.ok(Map.of(
            "success", true,
            "user", Map.of(
                "email", authenticated.getEmail(),
                "role", authenticated.getRole(),
                "id", authenticated.getUserId()
            ),
            "token", "demo-token"
        ));
    }
}