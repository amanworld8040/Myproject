package project.OnlineTrainingProgram.Dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import project.OnlineTrainingProgram.Entity.TrainingProgram;
import project.OnlineTrainingProgram.Model.TrainingModel;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class TrainingDAO {
     //core jpa interface for the interaction with the db
    @PersistenceContext
    private EntityManager entityManager;

    public void save(TrainingModel trainingModel) {
        try {
        	//if programId is 0 than a new training Prog need to be created
            if (trainingModel.getProgramId() == 0) { 
                //converts trainingModel in jpa  entity trainingProgram
                TrainingProgram entity = toEntity(trainingModel);
                //persist new entity to the db to perform insert operation
                entityManager.persist(entity);
                // Forces the pending changes in the db
                entityManager.flush(); 
            } else { 
            	// if programId is not 0, that an existing program is need to be updated
                TrainingProgram entity = entityManager.find(TrainingProgram.class, trainingModel.getProgramId());//finds exixting entity in db using pk
                //checks if id found
                if (entity != null) {
                	//than update this field with the new values
                    entity.setProgramName(trainingModel.getProgramName());
                    entity.setDescription(trainingModel.getDescription());
                    entity.setPrice(trainingModel.getPrice());
                    entity.setProgramStatus(		
                    TrainingProgram.ProgramStatus.valueOf(trainingModel.getStatus().toUpperCase())//convert status from string to following ENUM values
                    );
                    entityManager.merge(entity);//merges the updated entity with persistance context
                    entityManager.flush();//do the update to be committed in db                 
                    }
            }
        } catch (Exception e) {
        	//Prints the stack trace if an error occurs during the save operation for debugging.
            e.printStackTrace(); 
        }
    }

    public TrainingModel getTrainingById(int id) {
    	//Finds a TrainingProgram entity by its ID.
        TrainingProgram entity = entityManager.find(TrainingProgram.class, id);
        //Returns the converted model otherwise returns null
        return entity != null ? toModel(entity) : null;
    }
  
    public TrainingModel getProgramById(int programId) {
        TrainingProgram entity = entityManager.find(TrainingProgram.class, programId);
        //Converts the entity to a model or returns null.
        return entity != null ? toModel(entity) : null;
    }
    
    public List<TrainingModel> getAllTrainings() {
    	//Creates a JPQL query to select all records from the TrainingProgram entity.
        List<TrainingProgram> programs =
            entityManager.createQuery("SELECT t FROM TrainingProgram t", TrainingProgram.class)
                         .getResultList();
        //Uses a stream to map each TrainingProgram entity to a TrainingModel and collects them into a new List.
        return programs.stream().map(this::toModel).collect(Collectors.toList());
    }

    public void delete(int id) {
        TrainingProgram program = entityManager.find(TrainingProgram.class, id);
        if (program != null) {
            entityManager.remove(program);//remove program from db
        }
    }
    @Transactional
    public boolean deleteTrainingById(int programId) {
        TrainingProgram program = entityManager.find(TrainingProgram.class, programId);
        if (program != null) {
            entityManager.remove(program);//remove program from db
            return true;
        }
        return false;
    }

    
    private TrainingModel toModel(TrainingProgram entity) {
    	//method to convert a database entity to a model
        TrainingModel model = new TrainingModel();
        //maps all
        model.setProgramId(entity.getProgramId());
        model.setProgramName(entity.getProgramName());
        model.setDescription(entity.getDescription());
        model.setPrice(entity.getPrice());
        model.setStatus(entity.getProgramStatus().name());
        model.setPurchasedByCount(entity.getAllocations() != null ? entity.getAllocations().size() : 0);
        return model;
    }

    private TrainingProgram toEntity(TrainingModel model) {
    	//method to convert a model to a database entity.
        TrainingProgram entity = new TrainingProgram();
        //maps the program name
        entity.setProgramName(model.getProgramName());
        entity.setDescription(model.getDescription());
        entity.setPrice(model.getPrice());
        //Checks if the status is not null before converting it to an enum.
        if (model.getStatus() != null) {
        	//Converts the status string back to the ProgramStatus enum.
        	entity.setProgramStatus(
                TrainingProgram.ProgramStatus.valueOf(model.getStatus().toUpperCase())
            );
        } else {
        	//Sets a default status of ACTIVE if the model's status is null.
            entity.setProgramStatus(TrainingProgram.ProgramStatus.ACTIVE);
        }

        return entity;
    }
}
