package project.OnlineTrainingProgram.UI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import project.OnlineTrainingProgram.Model.UserTrainingAllocationModel;
import project.OnlineTrainingProgram.Service.UserTrainingAllocationService;

import java.util.List;

@RestController
@RequestMapping("/api/allocations")
@CrossOrigin(origins = "http://localhost:5173")
public class UserTrainingAllocationController {

    @Autowired
    private UserTrainingAllocationService allocationService;

    @PostMapping
    public String save(@RequestBody UserTrainingAllocationModel model) {
        allocationService.saveAllocation(model);
        return "Allocation saved successfully!";
    }

    @GetMapping("/{id}")
    public UserTrainingAllocationModel getById(@PathVariable int id) {
        return allocationService.getAllocationById(id);
    }

    @GetMapping
    public List<UserTrainingAllocationModel> getAll() {
        return allocationService.getAllAllocations();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        allocationService.deleteAllocation(id);
        return "Allocation deleted successfully!";
    }
}
