package project.OnlineTrainingProgram.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.OnlineTrainingProgram.Dao.UserTrainingAllocationDAO;
import project.OnlineTrainingProgram.Model.UserTrainingAllocationModel;


@Service
public class UserTrainingAllocationService {
    // Save ProgramAllocation entity directly
    public void saveAllocationEntity(project.OnlineTrainingProgram.Entity.ProgramAllocation allocation) {
        allocationDAO.saveEntity(allocation);
    }

    @Autowired
    private UserTrainingAllocationDAO allocationDAO;

    public void saveAllocation(UserTrainingAllocationModel allocationModel) {
        allocationDAO.save(allocationModel);
    }

    public UserTrainingAllocationModel getAllocationById(int id) {
        return allocationDAO.getAllocationById(id);
    }
    public List<project.OnlineTrainingProgram.Entity.ProgramAllocation> getAllocationsByUserId(int userId) {
        return allocationDAO.getAllocationsByUserId(userId);
    }
    
    public List<UserTrainingAllocationModel> getAllAllocations() {
        return allocationDAO.getAllAllocations();
    }

    public void deleteAllocation(int id) {
        allocationDAO.delete(id);
    }
    public boolean deleteAllocation(int userId, int trainingId) {
        project.OnlineTrainingProgram.Entity.ProgramAllocation allocation = allocationDAO.getByUserIdAndTrainingId(userId, trainingId);
        if (allocation != null) {
            allocationDAO.delete(allocation.getAllocationId());  // delete by allocation id
            return true;
        }
        return false;
    }

}
