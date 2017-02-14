package it.ifttt.services;

import java.util.List;

import org.bson.types.ObjectId;

import it.ifttt.domain.RecipeInstance;
import it.ifttt.domain.RecipeStruct;
import it.ifttt.exceptions.DatabaseException;

public interface RecipeService {
	
	void saveRecipeStruct(RecipeStruct recipeStruct) throws DatabaseException;
	List<RecipeStruct> getAllRecipesStruct();
	void deleteAllRecipesStruct() throws DatabaseException;
	
	void saveRecipeInstance(RecipeInstance recipeInstance) throws DatabaseException;
	List<RecipeInstance> getAllRecipesInstance();
	List<RecipeInstance> getAllActiveRecipesInstance();
	void activeRecipeInstance(ObjectId id) throws DatabaseException, IllegalArgumentException;
	void disableRecipeInstance(ObjectId id) throws DatabaseException, IllegalArgumentException;
	void updateRefreshTime(ObjectId id) throws DatabaseException, IllegalArgumentException;
	void deleteAllRecipesInstance() throws DatabaseException;
	void deleteRecipeInstance(ObjectId id) throws DatabaseException, IllegalArgumentException;
	
	
}
