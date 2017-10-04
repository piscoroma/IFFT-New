package it.ifttt.services;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import it.ifttt.domain.Action;
import it.ifttt.domain.RecipeInstance;
import it.ifttt.domain.RecipeStruct;
import it.ifttt.domain.Trigger;
import it.ifttt.exceptions.DatabaseException;

public interface RecipeService {
	
	RecipeStruct saveRecipeStruct(RecipeStruct recipeStruct) throws DatabaseException;
	void updateRecipeStruct(RecipeStruct recipeStruct) throws DatabaseException, IllegalArgumentException;
	List<RecipeStruct> getAllRecipesStruct();
	List<RecipeStruct> getAllPublicRecipesStruct();
	void deleteAllRecipesStruct() throws DatabaseException;
	RecipeStruct getRecipeStruct(ObjectId id_trigger, ObjectId id_action) throws DatabaseException;
	
	RecipeInstance saveRecipeInstance(RecipeInstance recipeInstance) throws DatabaseException;
	void updateRecipeInstance(RecipeInstance recipeInstance) throws DatabaseException, IllegalArgumentException;
	List<RecipeInstance> getAllRecipesInstance();
	List<RecipeInstance> getAllActiveRecipesInstance();
	void activeRecipeInstance(ObjectId id) throws DatabaseException, IllegalArgumentException;
	void disableRecipeInstance(ObjectId id) throws DatabaseException, IllegalArgumentException;
	void updateRefreshTime(ObjectId id) throws DatabaseException, IllegalArgumentException;
	void updateRefreshTime(ObjectId id, Date lastRefresh) throws DatabaseException, IllegalArgumentException;
	void deleteAllRecipesInstance() throws DatabaseException;
	void deleteRecipeInstance(ObjectId id) throws DatabaseException, IllegalArgumentException;
	
	
}
