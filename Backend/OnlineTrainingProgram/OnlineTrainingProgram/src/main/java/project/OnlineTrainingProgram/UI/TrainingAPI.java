package project.OnlineTrainingProgram.UI;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import project.OnlineTrainingProgram.Model.TrainingModel;
import project.OnlineTrainingProgram.Service.TrainingService;

@RestController
@RequestMapping("/api/training")
@CrossOrigin(origins = "http://localhost:5173")
public class TrainingAPI {
	
	@Autowired
	private TrainingService trainingService;
	
	@PostMapping
	public String saveTraining(@RequestBody TrainingModel trainingModel) {
		trainingService.saveTraining(trainingModel);
		return "Training program saved successfully!";		
	}
	
	@GetMapping("/{id}")
	public TrainingModel getTrainingById(@PathVariable int id) {
		return trainingService.getTrainingById(id);
	}
	
	@GetMapping
	public List<TrainingModel> getAllTrainings(){
		return trainingService.getAllTrainings();
		
	}
	
	@DeleteMapping("/{id}")
	public String deleteTraining(@PathVariable int id) {
		trainingService.deleteTraining(id);
		return "Training Program Deleted Successfully!";
	}
}
