package project.OnlineTrainingProgram.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.OnlineTrainingProgram.Dao.TrainingDAO;
import project.OnlineTrainingProgram.Model.TrainingModel;
import java.util.List;

@Service
public class TrainingService {
    @Autowired
    private TrainingDAO trainingDAO;
    
    public void saveTraining(TrainingModel trainingModel) {
        trainingDAO.save(trainingModel);
    }

    public TrainingModel getTrainingById(int id) {
        return trainingDAO.getTrainingById(id);
    }
    
	public TrainingModel  getProgramById(int programId) {
		return trainingDAO.getProgramById(programId);
	}
    public List<TrainingModel> getAllTrainings() {
        return trainingDAO.getAllTrainings();
    }
    public boolean removeTrainingProgram(int programId) {
    	try {
    		return trainingDAO.deleteTrainingById(programId);
    	} catch(Exception e) {
    		System.out.println("Error removing training program"+e.getMessage());
    		return false;
    	}
    }
    public void deleteTraining(int id) {
        trainingDAO.delete(id);
    }


}