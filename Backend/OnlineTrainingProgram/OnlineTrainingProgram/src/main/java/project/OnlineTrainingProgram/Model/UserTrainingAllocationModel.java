package project.OnlineTrainingProgram.Model;

import java.time.LocalDate;


public class UserTrainingAllocationModel {
 
	  
    private int allocationId;
    private int userId;
    private int programId;
    private int allocatedById;
    private LocalDate allocationDate;

    public UserTrainingAllocationModel() {}

    public UserTrainingAllocationModel(int allocationId, int userId, int programId,
                                       int allocatedById, LocalDate allocationDate) {
        this.allocationId = allocationId;
        this.userId = userId;
        this.programId = programId;
        this.allocatedById = allocatedById;
        this.allocationDate = allocationDate;
    }

    // Getters & Setters
    public int getAllocationId() { return allocationId; }
    public void setAllocationId(int allocationId) { this.allocationId = allocationId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getProgramId() { return programId; }
    public void setProgramId(int programId) { this.programId = programId; }

    public int getAllocatedById() { return allocatedById; }
    public void setAllocatedById(int allocatedById) { this.allocatedById = allocatedById; }

    public LocalDate getAllocationDate() { return allocationDate; }
    public void setAllocationDate(LocalDate allocationDate) { this.allocationDate = allocationDate; }
}
