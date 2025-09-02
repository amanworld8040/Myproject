package project.OnlineTrainingProgram.Model;

public class UserModel {

    private int userId;
    private String name;
    private String email;
    private String phoneNo;
    private String password;
    private String role;
    private int programCount; 

    public UserModel() {}
  
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNo() { return phoneNo; }
    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getProgramCount() { return programCount; }
    public void setProgramCount(int programCount) { this.programCount = programCount; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
}
