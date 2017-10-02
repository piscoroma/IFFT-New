package it.ifttt.controllers;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.ifttt.configuration.MongoObjectIdTypeAdapter;
import it.ifttt.domain.Action;
import it.ifttt.domain.Channel;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.RecipeClient;
import it.ifttt.domain.RecipeInstance;
import it.ifttt.domain.RecipeStruct;
import it.ifttt.domain.Trigger;
import it.ifttt.domain.User;
import it.ifttt.exceptions.ChannelNotFoundException;
import it.ifttt.exceptions.DatabaseException;
import it.ifttt.exceptions.InternalServerErrorException;
import it.ifttt.exceptions.NotFoundException;
import it.ifttt.services.ChannelService;
import it.ifttt.services.RecipeService;
import it.ifttt.services.UserService;

@RestController
@CrossOrigin
public class RestContollerImpl implements it.ifttt.controllers.RestController {

	private final static Logger log = Logger.getLogger(RestContollerImpl.class);
	Gson gson = new GsonBuilder().registerTypeAdapter(ObjectId.class, new MongoObjectIdTypeAdapter()).create();;
	
	@Autowired 
	ChannelService channelService;
	@Autowired 
	RecipeService recipeService;
	@Autowired 
	UserService userService;
	
	@Override
	@RequestMapping(value="/helloWorld", method=RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public String HelloWorld() {
		log.debug("This is a debug message");
        log.info("This is an info message");
        log.warn("This is a warn message");
        log.error("This is an error message");
		return "Hello World";
	}
	
	
	@RequestMapping(value="/authHelloWorld", method=RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public String HelloWorldAuth() {
		log.debug("Auth: This is a debug message");
        log.info("Auth: This is an info message");
        log.warn("Auth: This is a warn message");
        log.error("Auth: This is an error message");
		return "Auth: Hello World";
	}
	
	@Override
	//@CrossOrigin
	@RequestMapping(value="/channels", method=RequestMethod.GET, produces = {MediaType.APPLICATION_JSON})
	@ResponseStatus(value=HttpStatus.OK)
	public String getAllChannels() {
		List<Channel> channels = channelService.getAllChannels();
		return gson.toJson(channels);
	}
	
	@Override
	//@CrossOrigin
	@RequestMapping(value="/channels/{ch_id}", method=RequestMethod.GET, produces = {MediaType.APPLICATION_JSON})
	@ResponseStatus(value=HttpStatus.OK)
	public String getChannelById(@PathVariable ObjectId ch_id) {
		Channel channel;
		try {
			channel = channelService.getChannelById(ch_id);
			return gson.toJson(channel);
		} catch (NotFoundException e) {
			throw e;
		} catch (DatabaseException e) {
			throw new InternalServerErrorException();
		}
	}
	
	@Override
	//@CrossOrigin
	@RequestMapping(value="/triggers/{ch_id}", method=RequestMethod.GET, produces = {MediaType.APPLICATION_JSON})
	@ResponseStatus(value=HttpStatus.OK)
	public String getTriggersByChannelId(@PathVariable ObjectId ch_id) {
		List<Trigger> triggers;
		try {
			Channel channel = channelService.getChannelById(ch_id);
			triggers = channelService.getTriggersByChannel(channel);
		} catch (ChannelNotFoundException e) {
			throw e;
		} catch (DatabaseException e) {
			throw new InternalServerErrorException();
		}
		return gson.toJson(triggers);
	}
	
	@Override
	//@CrossOrigin
	@RequestMapping(value="/actions/{ch_id}", method=RequestMethod.GET, produces = {MediaType.APPLICATION_JSON})
	@ResponseStatus(value=HttpStatus.OK)
	public String getActionsByChannelId(@PathVariable ObjectId ch_id) {
		List<Action> actions;
		try {
			Channel channel = channelService.getChannelById(ch_id);
			actions = channelService.getActionsByChannel(channel);
		} catch (ChannelNotFoundException e) {
			throw e;
		} catch (DatabaseException e) {
			throw new InternalServerErrorException();
		}
		return gson.toJson(actions);
	}
	
	@Override
	//@CrossOrigin
	@RequestMapping(value="/recipes", method=RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.CREATED)
	public void postRecipe(@RequestBody String json_recipeClient){
		System.out.println("AAA" + json_recipeClient.toString());
		RecipeClient recipeClient = gson.fromJson(json_recipeClient, RecipeClient.class);
		System.out.println("BBB" + recipeClient.toString());
		RecipeStruct recipeStruct = recipeService.getRecipeStruct(recipeClient.getTrId(), recipeClient.getAcId());
		try{
			if(recipeStruct==null){
				User author = userService.getUserByUsername(recipeClient.getUsername());
				Trigger trigger = channelService.getTriggerById(recipeClient.getTrId());
				Action action = channelService.getActionById(recipeClient.getAcId());
				String description = recipeClient.getDescription();
				boolean isPublic = false;
				recipeStruct = new RecipeStruct(author, description, isPublic, trigger, action);
				recipeService.saveRecipeStruct(recipeStruct);
			}
			User user = userService.getUserByUsername(recipeClient.getUsername());
			boolean isActive = recipeClient.getIsActive();
			String title = recipeClient.getTitle();
			List<Ingredient> triggerIngredients = recipeClient.getIngrTr();
			List<Ingredient> actionIngredients = recipeClient.getIngrAc();
			RecipeInstance recipeInstance = new RecipeInstance(user, isActive, title, recipeStruct, triggerIngredients, actionIngredients);
			recipeService.saveRecipeInstance(recipeInstance);
			System.out.println("Ricetta salvata:");
			System.out.println("RecipeStruct: " + recipeStruct);
			System.out.println("RecipeInstance: " + recipeInstance);
		}catch(Exception e){
			throw new InternalServerErrorException();
		}
	}
	
	@Override
	//@CrossOrigin
	@RequestMapping(value="/recipesStruct/public", method=RequestMethod.GET, produces = {MediaType.APPLICATION_JSON})
	@ResponseStatus(value=HttpStatus.OK)
	public String getAllPublicRecipesStruct() {
		List<RecipeStruct> publicRecipeStructList = recipeService.getAllPublicRecipesStruct();
		return gson.toJson(publicRecipeStructList);
	}

	@Override
	//@CrossOrigin
	@RequestMapping(value="/recipesStruct/{id}", method=RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON})
	@ResponseStatus(value=HttpStatus.OK)
	public void updateRecipeStruct(@RequestBody String json_recipeInstance) {
		RecipeInstance recipeInstance = gson.fromJson(json_recipeInstance, RecipeInstance.class);
		RecipeStruct recipeStruct = recipeInstance.getRecipeStruct();
		try{
			recipeService.updateRecipeStruct(recipeStruct);
		}catch(IllegalArgumentException e){
			throw new NotFoundException();
		}catch(DatabaseException e){
			throw new InternalServerErrorException();
		}
	}
	
	@Override
	//@CrossOrigin
	@RequestMapping(value="/recipesInstance/{id}", method=RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON})
	@ResponseStatus(value=HttpStatus.OK)
	public void updateRecipeInstance(@RequestBody String json_recipeInstance) {
		RecipeInstance recipeInstance = gson.fromJson(json_recipeInstance, RecipeInstance.class);
		try{
			recipeService.updateRecipeInstance(recipeInstance);
		}catch(IllegalArgumentException e){
			throw new NotFoundException();
		}catch(DatabaseException e){
			throw new InternalServerErrorException();
		}
	}
	
	
	
}