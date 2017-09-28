package it.ifttt.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import it.ifttt.domain.RecipeStruct;

@Repository
public interface RecipeStructRepository extends MongoRepository<RecipeStruct, Long> {
	List<RecipeStruct> findByIsPublic(boolean isPublic);
	RecipeStruct findById(ObjectId id);
}
