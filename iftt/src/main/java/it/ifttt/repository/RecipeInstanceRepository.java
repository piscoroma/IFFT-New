package it.ifttt.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import it.ifttt.domain.RecipeInstance;
import it.ifttt.domain.User;

@Repository
public interface RecipeInstanceRepository extends MongoRepository<RecipeInstance, Long> {
	RecipeInstance findById(ObjectId id);
	List<RecipeInstance> findByIsActive(boolean IsActive);
	List<RecipeInstance> findByUser(User user);
}
