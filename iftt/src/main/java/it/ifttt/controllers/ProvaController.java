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
		RecipeInstance recipeInstance;
		List<Ingredient> triggerIngredients;
		List<Ingredient> actionIngredients;
		
		/** GCALENDAR -> GMAIL **/
		/** if a new event is created, send me an email **/
		recipeStruct.setDescription("if a new event is created, send me a mail");
		recipeStruct.setPublic(true);
		recipeStruct.setTrigger(channelService.getTriggerByName("CALENDAR_EVENT_CREATED"));
		recipeStruct.setAction(channelService.getActionByName("SEND_EMAIL"));
		recipeInstance = new RecipeInstance();
		recipeInstance.setRecipeStruct(recipeStruct);
		recipeInstance.setUser(user);
		recipeInstance.setActive(true);
		triggerIngredients = new ArrayList<Ingredient>();
		triggerIngredients.add(new Ingredient("summary", "open-day"));
		triggerIngredients.add(new Ingredient("description", "open day, tutti invitati"));
		triggerIngredients.add(new Ingredient("location", "politecnico"));
		triggerIngredients.add(new Ingredient("creator", "piscoroma@gmail.com"));
		actionIngredients = new ArrayList<Ingredient>();
		actionIngredients.add(new Ingredient("to", "piscoroma@gmail.com"));
		actionIngredients.add(new Ingredient("subject", "new event added description: @DESCRIPTION , location: @LOCATION"));
		actionIngredients.add(new Ingredient("body", "event created by @CREATOR_MAIL . See you there."));
		
		/** GCALENDAR -> TWITTER **/
		/** if a new event is created, tweet me **/
		recipeStruct.setDescription("if a new event is created, tweet me");
		recipeStruct.setPublic(true);
		recipeStruct.setTrigger(channelService.getTriggerByName("CALENDAR_EVENT_CREATED"));
		recipeStruct.setAction(channelService.getActionByName("TWEET_STATE_ACTION"));
		recipeInstance = new RecipeInstance();
		recipeInstance.setRecipeStruct(recipeStruct);
		recipeInstance.setUser(user);
		recipeInstance.setActive(true);
		triggerIngredients = new ArrayList<Ingredient>();
		triggerIngredients.add(new Ingredient("summary", "cena tra colleghi"));
		triggerIngredients.add(new Ingredient("description", "tutti a mangiare la pizza"));
		triggerIngredients.add(new Ingredient("location", "pizzeria 4 soldi"));
		triggerIngredients.add(new Ingredient("creator", "piscoroma@gmail.com"));
		actionIngredients = new ArrayList<Ingredient>();
		actionIngredients.add(new Ingredient("text", "event: @SUMMARY created at @LOCATION by @CREATOR_NAME"));

		/** GMAIL -> GCALENDAR **/
		/** if receive an email, add event on calendar **/
		recipeStruct.setDescription("if receive an email, add event on calendar");
		recipeStruct.setPublic(true);
		recipeStruct.setTrigger(channelService.getTriggerByName("EMAIL_RECEIVED"));
		recipeStruct.setAction(channelService.getActionByName("CALENDAR_CREATE_EVENT"));
		recipeInstance = new RecipeInstance();
		recipeInstance.setRecipeStruct(recipeStruct);
		recipeInstance.setUser(user);
		recipeInstance.setActive(true);
		triggerIngredients = new ArrayList<Ingredient>();
		triggerIngredients.add(new Ingredient("from", "piscoroma@gmail.com"));
		triggerIngredients.add(new Ingredient("subject", "evento"));
		actionIngredients = new ArrayList<Ingredient>();
		actionIngredients.add(new Ingredient("summary", "@SUBJECT from @SENDER"));
		actionIngredients.add(new Ingredient("description", "il body dell'email è: @BODY"));
		actionIngredients.add(new Ingredient("location", "da definire"));
		actionIngredients.add(new Ingredient("all-day", "true"));

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
