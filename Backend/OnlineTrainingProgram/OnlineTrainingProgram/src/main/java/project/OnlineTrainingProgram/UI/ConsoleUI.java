package project.OnlineTrainingProgram.UI;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import project.OnlineTrainingProgram.Model.TrainingModel;
import project.OnlineTrainingProgram.Model.UserModel;
import project.OnlineTrainingProgram.Model.UserTrainingAllocationModel;
import project.OnlineTrainingProgram.Service.TrainingService;
import project.OnlineTrainingProgram.Service.UserService;
import project.OnlineTrainingProgram.Service.UserTrainingAllocationService;


@Component
public class ConsoleUI implements CommandLineRunner {

    @Autowired
    private UserService userService;
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private UserTrainingAllocationService allocationService;

    @Override
    public void run(String... args) {
      
        try (Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("\n\n========== Online Training Program ==========");
                System.out.println("1. Login");
                System.out.println("2. Sign Up");
                System.out.println("3. Exit");
                System.out.print("Enter Your Choice: ");

                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    switch (choice) {
                        case 1 -> login(scanner);
                        case 2 -> signup(scanner);
                        case 3 -> {
                            System.out.println("Exiting... Goodbye! Thank you for using.");
                            System.exit(0); 
                        }
                        default -> System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("That's not a valid number! Please enter a valid menu option.");
                    scanner.nextLine(); 
                }
            }
        }
    }
   
    private void login(Scanner scanner) {
        System.out.println("\n--- User Login ---");
        try {
        	 String email;
             while (true) {
                 System.out.print("Enter email: ");
                 email = scanner.nextLine().trim();  
                 if (!isValidEmail(email)) {
                     System.out.println("Invalid email format. Please enter a valid email.");
                     continue;
                 }
                 break;
             }

             String password;
             while (true) {
                 System.out.print("Enter password: ");
                 password = scanner.nextLine().trim(); 
                 if (password.isEmpty()) {
                     System.out.println("Password cannot be empty. Please enter your password.");
                     continue;
                 } else if (password.length() < 5) {
                     System.out.println("Password must be at least 5 characters long.");
                     continue;
                 }
                 break;
             }

            UserModel user = userService.authenticate(email, password);

            if (user != null) {
                System.out.println("\nLogin successful! Welcome, " + user.getName() + ".");
                if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                    adminDashboard(scanner, user);
                } else if("USER".equalsIgnoreCase(user.getRole())){
                    userDashboard(scanner, user);
                }
            } else {
                System.out.println("Invalid login credentials. Please check your email and password and try again.");
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred during login: " + e.getMessage());
        }
    }
    private boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
    }
    
    private void signup(Scanner scanner) {
        System.out.println("\n--- New User Sign Up ---");
        try {
            UserModel newUser = new UserModel();
            String name;
            String email;
            String phone;
            String password;
            String role;
            //name
            do {
                System.out.print("Enter Full Name: ");
                name = scanner.nextLine().trim();
                if (name.isEmpty()) {
                    System.out.println("Full Name cannot be Empty. Please Try Again.");
                } else if (name.matches(".*\\d.*")) {
                    System.out.println("Full Name cannot contain numbers. Please Try Again.");
                } else if (name.matches(".*[^a-zA-Z\\s].*")) {
                    System.out.println("Full Name cannot contain special characters. Please try again.");
                }
            } while (name.isEmpty() || name.matches(".*\\d.*") || name.matches(".*[^a-zA-Z\\s].*"));
            newUser.setName(name);
            
            // Email
            do {
                System.out.print("Enter Email: ");
                email = scanner.nextLine().trim();
                if (!isValidEmail(email)) {
                    System.out.println("Invalid email format. Please try again.");
                } 
                // Check for existing email here
                else if (userService.getUserByEmail(email) != null) {
                    System.out.println("This email is already in use. Please try a different email.");
                    email = ""; // Reset email to re-enter loop
                }
            } while (email.isEmpty() || !isValidEmail(email));
            newUser.setEmail(email);
            
            //PhoneNo
            do {
            System.out.print("Enter Phone: ");
            phone=scanner.nextLine().trim();
            if (!phone.matches("\\d+")) {
                System.out.println("Phone number must contain digits only. Please try again.");
            	}
            }while(!phone.matches("\\d+"));
            newUser.setPhoneNo(phone);
            
            //password
            do {
            System.out.print("Enter Password: ");
            password=scanner.nextLine().trim();
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty. Please enter your password.");
            }else if(password.length() < 5) {
                System.out.println("Password must be at least 5 characters long.");
                }
            }while(password.isEmpty() || password.length() < 5);
            newUser.setPassword(password);
            
            //Role
            do {
            System.out.print("Enter Role (ADMIN or USER): ");
            role=scanner.nextLine().trim().toUpperCase();
            if(!("ADMIN".equals(role) || "USER".equals(role))) {
                System.out.println("Invalid role. Role must be 'ADMIN' or 'USER'. Please try again.");
            	}
            }while(!("ADMIN".equals(role) || "USER".equals(role)));
            newUser.setRole(role);

      
            userService.saveUser(newUser);
            System.out.println("\nAccount created successfully! You can now log in.");
        } catch (Exception e) {
            System.out.println("An error occurred while signing up: " + e.getMessage());
        }
    }

    private void userDashboard(Scanner scanner, UserModel user) {
        while (true) {
            System.out.println("\n--- User Dashboard ---");
            System.out.println("1. View Available Trainings");
            System.out.println("2. Enroll in a Training");
            System.out.println("3. View My Trainings");
            System.out.println("4. Cancel My Enrollment");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> viewAvailableTrainings();
                    case 2 -> enrollInTraining(scanner, user);
                    case 3 -> viewMyTraining(user);
                    case 4 -> cancelEnrollment(scanner, user);
                    case 5 -> {
                        System.out.println("Logged out successfully. Redirecting to main menu...");
                        return;
                    }
                    default -> System.out.println("Invalid option. Please enter a valid number.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number from the menu.");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("An unexpected error occurred in the dashboard: " + e.getMessage());
            }
        }
    }

   
    public void adminDashboard(Scanner scanner, UserModel user) {
        while (true) {
            System.out.println("\n--- Admin Dashboard ---");
            System.out.println("1. Create Training Program");
            System.out.println("2. View All Available Training Program");
            System.out.println("3. View All User");
            System.out.println("4. View Users with less than 3 Programs");
            System.out.println("5. Update Training Program Status");
            System.out.println("6. Allocate Program To User");
            System.out.println("7. Remove User From Training Program");
            System.out.println("8. Remove Training Program");
            System.out.println("9. Logout");
            System.out.print("Enter choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> createTrainingProgram(scanner,user);
                    case 2 -> viewTrainings();
                    case 3 -> viewAllUsers();
                    case 4 -> viewUsersByProgramCount();
                    case 5 -> updateTrainingProgramStatus(scanner);
                    case 6 -> allocateProgramToUser(scanner,user);
                    case 7 -> removeUserFromTrainingProgram(scanner);
                    case 8 -> removeTrainingProgram(scanner);
                    case 9 -> {
                        System.out.println("Logged out successfully. Redirecting to main menu...");
                        return;
                    }
                    default -> System.out.println("Invalid option. Please enter a valid number.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number from the menu.");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("An unexpected error occurred in the dashboard: " + e.getMessage());
            }
        }
    }
    
    private void viewAvailableTrainings() {
        try {
            System.out.println("\n----- List of Available Trainings -----");
            var trainings = trainingService.getAllTrainings();

            if (trainings.isEmpty()) {
                System.out.println("No trainings are currently available.");
                return;
            }

            trainings.sort(Comparator.comparingInt(TrainingModel::getProgramId));

            String formatString = "| %-5s | %-25s | %-40s | %-10s | %-9s |%n";

            System.out.printf(formatString, "ID", "Name", "Description", "Price", "Status");
            System.out.println("---------------------------------------------------------------------------------------------------------");

            for (TrainingModel t : trainings) {
                String name = wrapText(t.getProgramName(), 25);
                String desc = wrapText(t.getDescription(), 40);

                String[] nameLines = name.split("\n");
                String[] descLines = desc.split("\n");
                int maxLines = Math.max(nameLines.length, descLines.length);

                for (int i = 0; i < maxLines; i++) {
                    System.out.printf(
                        formatString,
                        (i == 0 ? t.getProgramId() : ""), 
                        (i < nameLines.length ? nameLines[i] : ""),
                        (i < descLines.length ? descLines[i] : ""),
                        (i == 0 ? String.format("%.1f", t.getPrice()) : ""),
                        (i == 0 ? t.getStatus() : "")
                    );
                }
                System.out.println("---------------------------------------------------------------------------------------------------------");

            }
        } catch (Exception e) {
            System.out.println("Error loading trainings: " + e.getMessage());
        }
    }


    private void viewTrainings() {
        try {
            System.out.println("\n----- List of Trainings -----");
            var trainings = trainingService.getAllTrainings();

            if (trainings.isEmpty()) {
                System.out.println("No trainings are currently available.");
                return;
            }

            trainings.sort(Comparator.comparingInt(TrainingModel::getProgramId));

            String formatString = "| %-5s | %-25s | %-40s | %-10s | %-12s | %-12s |%n";

            System.out.printf(formatString, "ID", "Name", "Description", "Price", "Status", "Purchased By");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------");

            for (TrainingModel t : trainings) {
                String name = wrapText(t.getProgramName(), 25);
                String desc = wrapText(t.getDescription(), 40);

                // Split into multiple lines
                String[] nameLines = name.split("\n");
                String[] descLines = desc.split("\n");
                int maxLines = Math.max(nameLines.length, descLines.length);

                for (int i = 0; i < maxLines; i++) {
                    System.out.printf(
                        formatString,
                        (i == 0 ? t.getProgramId() : ""), 
                        (i < nameLines.length ? nameLines[i] : ""),
                        (i < descLines.length ? descLines[i] : ""),
                        (i == 0 ? String.format("%.1f", t.getPrice()) : ""),
                        (i == 0 ? t.getStatus() : ""),
                        (i == 0 ? t.getPurchasedByCount() : "")
                    );
                }
                System.out.println("---------------------------------------------------------------------------------------------------------------------------");

            }
        } catch (Exception e) {
            System.out.println("Error loading trainings: " + e.getMessage());
        }
    }

    private String wrapText(String text, int maxLength) {
        if (text == null) return "";
        StringBuilder wrapped = new StringBuilder();
        String[] words = text.split(" ");
        int lineLen = 0;
        for (String word : words) {
            if (lineLen + word.length() > maxLength) {
                wrapped.append("\n");
                lineLen = 0;
            }
            wrapped.append(word).append(" ");
            lineLen += word.length() + 1;
        }
        return wrapped.toString().trim();
    }

   
    private void viewMyTraining(UserModel user) {
        try {
            var allocations = allocationService.getAllAllocations()
                .stream()
                .filter(a -> a.getUserId() == user.getUserId())
                .toList();

            if (allocations.isEmpty()) {
                System.out.println("No trainings have been allocated to you yet.");
                return;
            }

            record Row(int programId, String programName, String allocatedOn) {}

            var rows = allocations.stream()
                .map(a -> {
                    var tm = trainingService.getTrainingById(a.getProgramId());
                    String name = (tm != null && tm.getProgramName() != null) ? tm.getProgramName() : "Unknown";
                    return new Row(a.getProgramId(), name, String.valueOf(a.getAllocationDate()));
                })
                .sorted(Comparator.comparingInt(Row::programId)) // sort by Program ID
                .toList();

            System.out.println("\n--- My Trainings ---");
            System.out.printf("%-12s %-40s %-8s%n", "Program ID", "Program Name", "Allocated On");
            System.out.println("--------------------------------------------------------------------");
            rows.forEach(r ->
                System.out.printf("%-12d %-40s %-12s%n", r.programId(), r.programName(), r.allocatedOn())
            );

        } catch (Exception e) {
            System.out.println("Error loading your allocations: " + e.getMessage());
        }
    }
    record AllocationView(int programId, String programName, String allocatedOn) {}

    
    private void cancelEnrollment(Scanner scanner, UserModel user) {
        System.out.println("\n--- Cancel My Enrollment ---");
        List<UserTrainingAllocationModel> myEnrollments = allocationService.getAllAllocations()
                .stream()
                .filter(a -> a.getUserId() == user.getUserId())
                .toList();

        if (myEnrollments.isEmpty()) {
            System.out.println("You have no enrollments to cancel.");
            return;
        }

        record Row(int programId, String programName, String allocatedOn) {}

        var rows = myEnrollments.stream()
            .map(a -> {
                var tm = trainingService.getTrainingById(a.getProgramId());
                String name = (tm != null && tm.getProgramName() != null) ? tm.getProgramName() : "Unknown";
                return new Row(a.getProgramId(), name, String.valueOf(a.getAllocationDate()));
            })
            .sorted(Comparator.comparingInt(Row::programId))
            .toList();

        System.out.printf("%-5s %-12s %-32s %-20s%n", "No.", "Program ID", "Program Name", "Allocated On");
        System.out.println("----------------------------------------------------------------------------");

        for (int i = 0; i < rows.size(); i++) {
            Row r = rows.get(i);
            System.out.printf("%-5d %-12d %-40s %-12s%n",
                    (i + 1),
                    r.programId(),
                    r.programName(),
                    r.allocatedOn()
            );
        }
        System.out.print("Enter number to cancel: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > myEnrollments.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        allocationService.deleteAllocation(myEnrollments.get(choice - 1).getAllocationId());
        System.out.println("Enrollment cancelled successfully.");
    }

    private void enrollInTraining(Scanner scanner, UserModel user) {
        try {
            var trainings = trainingService.getAllTrainings();
            if (trainings.isEmpty()) {
                System.out.println("No trainings available to enroll in at this time.");
                return;
            }

            System.out.println("\n--- Available Trainings to Enroll ---");

            trainings.sort(Comparator.comparingInt(TrainingModel::getProgramId));

            String formatString = "%-12s %-40s %-12s %-12s%n";

            System.out.printf(formatString, "Program ID", "Program Name", "Price", "Status");
            System.out.println("----------------------------------------------------------------------------");

            for (TrainingModel t : trainings) {
                System.out.printf(
                    formatString,
                    t.getProgramId(),
                    (t.getProgramName() != null ? t.getProgramName() : "N/A"),
                    String.format("%.2f", t.getPrice()),
                    t.getStatus()
                );
            }
            System.out.println("----------------------------------------------------------------------------");

            System.out.print("Enter the Training ID to enroll: ");
            int programId = scanner.nextInt();
            scanner.nextLine();
 
            // Check if the training exists and get its details
            TrainingModel selectedProgram = trainingService.getTrainingById(programId);
            if (selectedProgram == null) {
                System.out.println("Training Program with ID " + programId + " not found.");
                return;
            }

            // Check if the training is completed or cancelled
            String status = selectedProgram.getStatus();
            if ("COMPLETED".equalsIgnoreCase(status) || "CANCELLED".equalsIgnoreCase(status)) {
                System.out.println("This training program has a status of " + status + ". You cannot enroll.");
                return;
            }
            
            UserTrainingAllocationModel allocation = new UserTrainingAllocationModel();
            allocation.setUserId(user.getUserId());
            allocation.setProgramId(programId);
            allocation.setAllocatedById(user.getUserId());
            allocation.setAllocationDate(LocalDate.now());

            allocationService.saveAllocation(allocation);
            System.out.println("Successfully enrolled in training with ID: " + programId);

        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number for the Training ID.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Failed to enroll in the training: " + e.getMessage());
        }
    }
  
    //USER DASHBOARD
    private void createTrainingProgram(Scanner scanner, UserModel model) {
        try {
            System.out.print("Enter Training Name: ");
            String programName = scanner.nextLine().trim();
            if (programName.isEmpty()) {
                System.out.println("Training name cannot be empty.");
                return;
            }

            System.out.print("Enter Description: ");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                System.out.println("Description cannot be empty.");
                return;
            }

            double price;
            String priceInput;
            do {
                System.out.print("Enter Price: ");
                priceInput = scanner.nextLine().trim();
                try {
                    price = Double.parseDouble(priceInput);
                    if (price < 0) {
                        System.out.println("Price cannot be negative. Please enter a valid price.");
                    } else {
                        break; 
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price format. Please enter a valid price");
                    price = -1.0; 
                }
            } while (true);

            String status;
            System.out.print("Enter Status (ACTIVE/COMPLETED/CANCELLED): ");
            do {
            status = scanner.nextLine().trim().toUpperCase();
            if(!status.equals("ACTIVE") && !status.equals("COMPLETED") && !status.equals("CANCELLED")) {
            	System.out.println("Status must be any of these 3. ACTIVE/COMPLETED/CANCELLED");
            	}
            }while(!status.equals("ACTIVE") && !status.equals("COMPLETED") && !status.equals("CANCELLED"));

            TrainingModel m = new TrainingModel();
            m.setProgramName(programName);
            m.setDescription(description);
            m.setPrice(price);
            m.setStatus(status);
            trainingService.saveTraining(m);

            System.out.println("Training Program created successfully.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private void updateTrainingProgramStatus(Scanner scanner) {
        System.out.print("Enter Training ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 
        
        TrainingModel m =trainingService.getTrainingById(id);
        if(m == null) {
        System.out.print("Training Not Found.");
        return;     
        }
        String status;
        System.out.print("Enter new Status (ACTIVE/COMPLETED/CANCELLED): ");
        do {
        status = scanner.nextLine().trim().toUpperCase();
        if(!status.equals("ACTIVE") && !status.equals("COMPLETED") && !status.equals("CANCELLED")) {
        	System.out.println("Status must be any of these 3. ACTIVE/COMPLETED/CANCELLED");
        	}
        }while(!status.equals("ACTIVE") && !status.equals("COMPLETED") && !status.equals("CANCELLED"));
        m.setStatus(status);
        
        trainingService.saveTraining(m);
        System.out.println("Training Program status updated.");
    }

    private void viewAllUsers() {
        List<UserModel> allUsers = userService.getAllUsers();
        
        if (allUsers == null || allUsers.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        List<UserModel> users = allUsers.stream()
                                        .filter(u -> "USER".equalsIgnoreCase(u.getRole()))
                                        .sorted(Comparator.comparingInt(UserModel::getUserId))
                                        .collect(Collectors.toList());

        if (users.isEmpty()) {
            System.out.println("No users with the 'USER' role found.");
            return;
        }

        String formatString = "| %-5s | %-15s | %-18s | %-12s | %-10s | %-15s |%n";
        System.out.println("--- Users ---");
        System.out.printf(formatString, "ID", "Name", "Email", "Phone", "Role", "Program Count");
        System.out.println("---------------------------------------------------------------------------------------------");

        for (UserModel u : users) {
            System.out.printf(
                formatString,
                u.getUserId(),
                u.getName(),
                u.getEmail(),
                u.getPhoneNo(),
                u.getRole(),
                u.getProgramCount()
            );
        }
        System.out.println("---------------------------------------------------------------------------------------------");
    }

    private void allocateProgramToUser(Scanner scanner, UserModel currentAdmin) {
        int userId = -1;
        int programId = -1;
        boolean inputValid = false;
        do {
            try {
                System.out.print("Enter User ID: ");
                userId = scanner.nextInt();
                if (userService.getUserById(userId) != null) {
                    inputValid = true;
                } else {
                    System.out.println("User with ID " + userId + " not found. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number for User ID.");
                scanner.nextLine(); 
            }
        } while (!inputValid);

        inputValid = false; 
        scanner.nextLine(); 
        TrainingModel selectedProgram = null;

        do {
            try {
                System.out.print("Enter Training Program ID: ");
                programId = scanner.nextInt();
                selectedProgram = trainingService.getProgramById(programId);
                
                if(selectedProgram != null) {
                	inputValid= true;
                }else {
                	System.out.println("Training Program with ID " + programId + " not found. Please try again.");
                }   
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number for Program ID.");
                scanner.nextLine(); 
            }
        } while (!inputValid);

        scanner.nextLine(); 

        try {
            if (selectedProgram == null) {
                System.out.println("Selected training program not found. Cannot allocate.");
                return;
            }
            String status = selectedProgram.getStatus();
            if ("COMPLETED".equalsIgnoreCase(status)) {
                System.out.println("This training program has already been COMPLETED. Cannot allocate.");
                return;
            } else if ("CANCELLED".equalsIgnoreCase(status)) {
                System.out.println("This training program has been CANCELLED. Cannot allocate.");
                return;
            }
            UserTrainingAllocationModel a = new UserTrainingAllocationModel();
            a.setUserId(userId);
            a.setProgramId(programId);
            a.setAllocatedById(currentAdmin.getUserId());
            a.setAllocationDate(LocalDate.now());

            allocationService.saveAllocation(a);
            System.out.println("Program allocated to user successfully.");
        } catch (Exception e) {
            System.out.println("Error: Failed to allocate program to user. " + e.getMessage());
            // Log the exception for debugging purposes.
        }
    }


    private void removeUserFromTrainingProgram(Scanner scanner) {
        try {
            System.out.print("Enter User ID: ");
            int userId = scanner.nextInt();
            System.out.print("Enter Training Program ID: ");
            int programId = scanner.nextInt();
            scanner.nextLine();

            List<UserTrainingAllocationModel> all = allocationService.getAllAllocations();
            if (all == null || all.isEmpty()) {
                System.out.println("NO Allocation Found.");
                return;
            }

            UserTrainingAllocationModel match = all.stream()
                    .filter(x -> x.getUserId() == userId && x.getProgramId() == programId)
                    .findFirst()
                    .orElse(null);

            if (match == null) {
                System.out.println("No Enrollment Found For The Given User and program");
                return;
            }
            allocationService.deleteAllocation(match.getAllocationId());
            System.out.println("User removed from training program.");
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number for User ID and Program ID.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
    private void removeTrainingProgram(Scanner scanner) {
        int programId;
        try {
            System.out.print("Enter Training Program ID to remove: ");
            programId = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            boolean removed = trainingService.removeTrainingProgram(programId);

            if (removed) {
                System.out.println("Training Program with ID " + programId + " removed successfully.");
            } else {
                System.out.println("Training Program not found or could not be removed.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number for the Training Program ID.");
            scanner.nextLine(); 
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while removing the training program: " + e.getMessage());
            
        }
    }
    private void viewUsersByProgramCount() {
        try {
            List<UserModel> allUsers = userService.getAllUsers();

            if (allUsers == null || allUsers.isEmpty()) {
                System.out.println("No users found.");
                return;
            }

            List<UserModel> usersWithLessThanThreePrograms = allUsers.stream()
                    .filter(u -> "USER".equalsIgnoreCase(u.getRole()) && u.getProgramCount() < 3)
                    .sorted(Comparator.comparingInt(UserModel::getUserId))
                    .collect(Collectors.toList());

            if (usersWithLessThanThreePrograms.isEmpty()) {
                System.out.println("All users have 3 or more programs allocated.");
                return;
            }

            String formatString = "| %-5s | %-20s | %-15s %n";
            System.out.println("\n--- Users with less than 3 Programs ---");
            System.out.printf(formatString, "ID", "Name", "Program Count");
            System.out.println("----------------------------------------------");

            for (UserModel u : usersWithLessThanThreePrograms) {
                System.out.printf(
                    formatString,
                    u.getUserId(),
                    u.getName(),
                    u.getProgramCount()
                );
            }
            System.out.println("----------------------------------------------");
        } catch (Exception e) {
            System.out.println("An error occurred while fetching users: " + e.getMessage());
        }
    }

}
