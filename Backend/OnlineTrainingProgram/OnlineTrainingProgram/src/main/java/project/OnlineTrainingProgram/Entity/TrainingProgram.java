package project.OnlineTrainingProgram.Entity;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "training_program")
public class TrainingProgram {

    public List<ProgramAllocation> getAllocations() {
		return allocations;
	}

	public void setAllocations(List<ProgramAllocation> allocations) {
		this.allocations = allocations;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_id")
    private int programId;
	
	@Column(name="Program_name")
	private String programName;

	@Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double price;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<ProgramAllocation> allocations;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProgramStatus programStatus = ProgramStatus.ACTIVE;

    public enum ProgramStatus {
        ACTIVE,
        COMPLETED,
        CANCELLED,
    }
   
    public int getProgramId() {
        return programId;
    }
    
    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProgramStatus getProgramStatus() {
        return programStatus;
    }

    public void setProgramStatus(ProgramStatus programStatus) {
        this.programStatus = programStatus;
    }
    
    public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}
}
