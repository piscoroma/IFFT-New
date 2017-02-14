package it.ifttt.controllers;

import java.util.List;

import org.bson.types.ObjectId;

import it.ifttt.domain.Action;
import it.ifttt.domain.Channel;
import it.ifttt.domain.RecipeInstance;
import it.ifttt.domain.RecipeStruct;
import it.ifttt.domain.Trigger;

public interface RestController {

	String HelloWorld();
	
	/*List<Channel> getAllChannels();
	List<Trigger> getAllTriggers();
	List<Action> getAllActions();
	
	RecipeStruct postRecipeStruct(RecipeStruct recipeStruct);
	List<RecipeStruct> getAllRecipesStructByUser(String username);
	List<RecipeStruct> getAllPublicRecipesStruct();
	RecipeStruct getRecipeStructById(ObjectId id);
	void publicRecipeStruct(ObjectId id);
	
	RecipeInstance postRecipeInstance(RecipeInstance recipeInstance);
	List<RecipeInstance> getAllRecipesInstanceByUser(String username);
	RecipeInstance getRecipeInstanceById(ObjectId id);
	void deleteRecipeInstance(ObjectId id);
	void activeRecipeInstance(ObjectId id);
	void disableRecipeInstance(ObjectId id);*/

	
	
}
