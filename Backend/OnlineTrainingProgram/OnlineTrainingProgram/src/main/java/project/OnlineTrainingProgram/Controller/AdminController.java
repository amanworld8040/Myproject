package project.OnlineTrainingProgram.Controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.OnlineTrainingProgram.Model.TrainingModel;
import project.OnlineTrainingProgram.Model.UserModel;
import project.OnlineTrainingProgram.Service.TrainingService;
import project.OnlineTrainingProgram.Service.UserService;
import project.OnlineTrainingProgram.Service.UserTrainingAllocationService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private UserTrainingAllocationService allocationService;


    //  Get all users
    @GetMapping("/users")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    //  Get all trainings
    @GetMapping("/trainings")
    public ResponseEntity<List<TrainingModel>> getAllTrainings() {
        return ResponseEntity.ok(trainingService.getAllTrainings());
    }

    //  Add new training
    @PostMapping("/trainings")
    public ResponseEntity<?> addTraining(@RequestBody TrainingModel training) {
        trainingService.saveTraining(training);
        return ResponseEntity.ok(Map.of("success", true, "message", "Training added successfully"));
    }

    // Delete training
    @DeleteMapping("/trainings/{programId}")
    public ResponseEntity<?> deleteTraining(@PathVariable int programId) {
        trainingService.deleteTraining(programId);
        return ResponseEntity.ok(Map.of("success", true, "message", "Training deleted successfully"));
    }

    //  Delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("success", true, "message", "User deleted successfully"));
    }

    //  View all allocations
    @GetMapping("/allocations")
    public ResponseEntity<?> getAllAllocations() {
        return ResponseEntity.ok(allocationService.getAllAllocations());
    }

    // Get users allocated with less than 3 programs (move up to avoid path conflict)
    @GetMapping("/users/allocations/under-allocated")
    public ResponseEntity<?> getUsersWithLessThanThreePrograms() {
        List<UserModel> allUsers = userService.getAllUsers();
        List<UserModel> result = new java.util.ArrayList<>();
        for (UserModel user : allUsers) {
            int count = allocationService.getAllocationsByUserId(user.getUserId()).size();
            if (count < 3) {
                user.setProgramCount(count);
                result.add(user);
            }
        }
        return ResponseEntity.ok(result);
    }

    // 8. Update training status by training ID
    @PutMapping("/trainings/{id}/status")
    public ResponseEntity<?> updateTrainingStatus(@PathVariable int id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        if (status == null || status.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Status is required"));
        }
        // Find the training in the existing trainings list, update status and save it.
        List<TrainingModel> trainings = trainingService.getAllTrainings();
        for (TrainingModel training : trainings) {
            if (training.getProgramId() == id) {
                training.setStatus(status);
                trainingService.saveTraining(training);
                return ResponseEntity.ok(Map.of("success", true, "message", "Training status updated successfully"));
            }
        }
        return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Training not found or status not updated"));
    }
        // 9. Allocate program to user
    @PostMapping("/allocate-program")
    public ResponseEntity<?> allocateProgramToUser(@RequestBody Map<String, String> body) {
        if (body == null || !body.containsKey("userId") || !body.containsKey("programId")) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "userId and programId required"));
        }
        int userId, programId;
        try {
            userId = Integer.parseInt(body.get("userId"));
            programId = Integer.parseInt(body.get("programId"));
        } catch (NumberFormatException nfe) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid numeric id"));
        }
        if (userService.getUserById(userId) == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "User not found"));
        }
        TrainingModel training = trainingService.getProgramById(programId);
        if (training == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Training not found"));
        }
        // Check existing allocations for user
        var existing = allocationService.getAllocationsByUserId(userId);
        if (existing != null && existing.stream().anyMatch(a -> a.getProgram().getProgramId() == programId)) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Already allocated"));
        }
        // Allocate
        project.OnlineTrainingProgram.Entity.User userEntity = new project.OnlineTrainingProgram.Entity.User();
        userEntity.setUserId(userId);
        project.OnlineTrainingProgram.Entity.TrainingProgram trainingEntity = new project.OnlineTrainingProgram.Entity.TrainingProgram();
        trainingEntity.setProgramId(programId);
        project.OnlineTrainingProgram.Entity.ProgramAllocation allocationEntity = new project.OnlineTrainingProgram.Entity.ProgramAllocation();
        allocationEntity.setUser(userEntity);
        allocationEntity.setProgram(trainingEntity);
        allocationEntity.setAllocationDate(java.time.LocalDate.now());
        allocationService.saveAllocationEntity(allocationEntity);
        return ResponseEntity.ok(Map.of("success", true, "message", "Program allocated to user successfully", "programId", programId));
    }

        // Delete allocation by userId and programId
    @DeleteMapping("/allocations/delete")
    public ResponseEntity<?> deleteAllocationByUserAndProgram(@RequestBody Map<String, String> body) {
        if (body == null || !body.containsKey("userId") || !body.containsKey("programId")) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "userId and programId required"));
        }
        int userId, programId;
        try {
            userId = Integer.parseInt(body.get("userId"));
            programId = Integer.parseInt(body.get("programId"));
        } catch (NumberFormatException nfe) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid numeric id"));
        }
        boolean existed = allocationService.deleteAllocation(userId, programId);
        if (!existed) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Enrollment not found"));
        }
        return ResponseEntity.ok(Map.of("success", true, "message", "Enrollment deleted successfully"));
    }


 
}
