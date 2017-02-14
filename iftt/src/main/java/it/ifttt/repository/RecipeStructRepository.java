package it.ifttt.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import it.ifttt.domain.RecipeStruct;

@Repository
public interface RecipeStructRepository extends MongoRepository<RecipeStruct, Long> {

}
