package project.OnlineTrainingProgram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnlineTrainingProgramApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(OnlineTrainingProgramApplication.class);
        app.run(args);
    }
}
