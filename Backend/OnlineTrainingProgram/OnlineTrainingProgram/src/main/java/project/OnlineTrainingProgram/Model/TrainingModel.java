package project.OnlineTrainingProgram.Model;

public class TrainingModel {

	private String ProgramName;;
    private int programId;
    private String description;
    private double price;
    private String status;
    private int purchasedByCount; 

    public TrainingModel() {}

    public TrainingModel(int programId,String ProgramName ,String description, double price, String status, int purchasedByCount) {
        this.programId = programId;
        this.description = description;
        this.price = price;
        this.status = status;
        this.purchasedByCount = purchasedByCount;
    }

    public int getProgramId() { return programId; }
    public void setProgramId(int programId) { this.programId = programId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getPurchasedByCount() { return purchasedByCount; }
    public void setPurchasedByCount(int purchasedByCount) { this.purchasedByCount = purchasedByCount; }

	public String getProgramName() { return ProgramName; }
	public void setProgramName(String programName) { ProgramName = programName; }
}
