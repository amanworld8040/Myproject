package project.OnlineTrainingProgram.Entity;

import java.time.LocalDate;

import jakarta.persistence.*;
@Entity
@Table(name = "user_program_allocation")
public class ProgramAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allocation_id")
    private int allocationId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private TrainingProgram program;

    @ManyToOne
    @JoinColumn(name = "allocated_by")
    private User allocatedBy;

    @Column(name = "allocation_date")
    private LocalDate allocationDate = LocalDate.now();



    public int getAllocationId() { return allocationId; }
    public void setAllocationId(int allocationId) { this.allocationId = allocationId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public TrainingProgram getProgram() { return program; }
    public void setProgram(TrainingProgram program) { this.program = program; }

    public User getAllocatedBy() { return allocatedBy; }
    public void setAllocatedBy(User allocatedBy) { this.allocatedBy = allocatedBy; }

    public LocalDate getAllocationDate() { return allocationDate; }
    public void setAllocationDate(LocalDate allocationDate) { this.allocationDate = allocationDate; }
}


