package it.ifttt.controllers;

import java.util.List;

import org.bson.types.ObjectId;

import it.ifttt.domain.Action;
import it.ifttt.domain.Channel;
import it.ifttt.domain.RecipeClient;
import it.ifttt.domain.RecipeInstance;
import it.ifttt.domain.RecipeStruct;
import it.ifttt.domain.Trigger;

public interface RestController {

	String HelloWorld();
	
	String getAllChannels();
	String getChannelById(ObjectId ch_id);
	String getTriggersByChannelId(ObjectId ch_id);
	String getActionsByChannelId(ObjectId ch_id);
	/*List<Trigger> getAllTriggers();
	List<Action> getAllActions();*/
	
	void postRecipe(String recipeClient);
	
	/*RecipeStruct postRecipeStruct(RecipeStruct recipeStruct);
	List<RecipeStruct> getAllRecipesStructByUser(String username);*/
	String getAllPublicRecipesStruct();
	//RecipeStruct getRecipeStructById(ObjectId id);
	void updateRecipeStruct(String recipeStruct);
	
	//RecipeInstance postRecipeInstance(RecipeInstance recipeInstance);
	//List<RecipeInstance> getAllRecipesInstanceByUser(String username);
	void updateRecipeInstance(String recipeInstance);
	/*RecipeInstance getRecipeInstanceById(ObjectId id);
	void deleteRecipeInstance(ObjectId id);
	void activeRecipeInstance(ObjectId id);
	void disableRecipeInstance(ObjectId id);*/

	
	
}
