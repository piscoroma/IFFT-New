package it.ifttt.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.ifttt.configuration.MongoObjectIdTypeAdapter;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.RecipeInstance;
import it.ifttt.domain.RecipeStruct;
import it.ifttt.domain.User;
import it.ifttt.services.ChannelService;
import it.ifttt.services.RecipeService;
import it.ifttt.services.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/prova")
public class ProvaController {
	
	private final static Logger log = Logger.getLogger(RestContollerImpl.class);
	Gson gson = new GsonBuilder().registerTypeAdapter(ObjectId.class, new MongoObjectIdTypeAdapter()).create();;
	
	@Autowired 
	ChannelService channelService;
	@Autowired 
	RecipeService recipeService;
	@Autowired 
	UserService userService;
	
	@RequestMapping(value="/recipe",method=RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.CREATED)
	public RecipeInstance postRecipe() {		
		
		User user = userService.getUserByUsername("giuseppe");
		
		RecipeStruct recipeStruct = new RecipeStruct();
		recipeStruct.setAuthor(user);
		recipeStruct.setDescription("if a new event is created, send me a mail");
		recipeStruct.setPublic(true);
		recipeStruct.setTrigger(channelService.getTriggerByName("CALENDAR_EVENT_CREATED"));
		recipeStruct.setAction(channelService.getActionByName("SEND_EMAIL"));
		recipeStruct = recipeService.saveRecipeStruct(recipeStruct);
		
		RecipeInstance recipeInstance = new RecipeInstance();
		recipeInstance.setRecipeStruct(recipeStruct);
		recipeInstance.setUser(user);
		recipeInstance.setActive(true);
		
		List<Ingredient> triggerIngredients = new ArrayList<Ingredient>();
		triggerIngredients.add(new Ingredient("location", "politecnico"));
		
		List<Ingredient> actionIngredients = new ArrayList<Ingredient>();
		actionIngredients.add(new Ingredient("to", "piscoroma@gmail.com"));
		actionIngredients.add(new Ingredient("subject", "new event added description: @DESCRIPTION , location: @LOCATION"));
		actionIngredients.add(new Ingredient("body", "event created by @CREATOR_MAIL . See you soon."));
		
		recipeInstance.setTriggerIngredients(triggerIngredients);
		recipeInstance.setActionIngredients(actionIngredients);
		
		recipeInstance.setTitle("se viene creato un evento al politecnico inviami un email");
		
		recipeInstance = recipeService.saveRecipeInstance(recipeInstance);
		if(recipeInstance.isActive())
			recipeService.activeRecipeInstance(recipeInstance.getId());
		
		return recipeInstance;
	}
	
	@RequestMapping(value="/recipe",method=RequestMethod.GET, produces = {MediaType.APPLICATION_JSON})
	@ResponseStatus(value=HttpStatus.CREATED)
	public String getRecipe() {		
		List<RecipeInstance> recipes = recipeService.getAllRecipesInstance();
		return gson.toJson(recipes);
	}
	
	@RequestMapping(value="/recipe/active", method=RequestMethod.GET, produces = {MediaType.APPLICATION_JSON})
	@ResponseStatus(value=HttpStatus.OK)
	public String getAllActiveRecipes() {
		List<RecipeInstance> activeRecipeList = recipeService.getAllActiveRecipesInstance();
		return gson.toJson(activeRecipeList);
	}
}
