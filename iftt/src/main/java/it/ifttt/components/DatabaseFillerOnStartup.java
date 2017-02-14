package it.ifttt.components;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import it.ifttt.domain.Action;
import it.ifttt.domain.Channel;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.RecipeInstance;
import it.ifttt.domain.RecipeStruct;
import it.ifttt.domain.Trigger;
import it.ifttt.domain.User;
import it.ifttt.exceptions.DatabaseException;
import it.ifttt.services.ChannelService;
import it.ifttt.services.RecipeService;
import it.ifttt.services.UserService;

@Component
public class DatabaseFillerOnStartup implements ApplicationListener<ContextRefreshedEvent> {

	private final static Logger log = Logger.getLogger(DatabaseFillerOnStartup.class);

	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
    UserService userService;
	@Autowired
	ChannelService channelService;
	@Autowired
    RecipeService recipeService;
	
	private static boolean dbLoad = false;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		if(dbLoad)
			return;
		
		log.debug("---I'm DatabaseFillerOnStartup---");
		//testQuery();
		try{
			clearDB();
			addUsers();
			addCollectionsChannel();
			addRecipesStruct();
			addRecipesInstance();
		}catch(DatabaseException | IllegalArgumentException e){
			log.debug("Exception: " + e.getMessage());
		}
		dbLoad = true;
		
	}
	
	private void testQuery(){
		
		MongoOperations mongoOps = mongoTemplate;

		User user = new User("giuseppe", "piscopo");
 		
		mongoOps.dropCollection(User.class);
		
	    // Insert is used to initially store the object into the database.
	 	mongoOps.insert(user);
	 	log.debug("insert user: " + user);

	     // Find
	 	user = mongoOps.findById(user.getId(), User.class);
	 	log.debug("found: " + user);

	     // Update
	 	mongoOps.updateFirst(query(where("username").is("giuseppe")), update("password","PISCOPO"), User.class);
	 	user = mongoOps.findOne(query(where("username").is("giuseppe")), User.class);
	 	log.debug("Updated: " + user);
	 	
	 	// Delete
	     mongoOps.remove(user);

	     // Check that deletion worked
	     List<User> users =  mongoOps.findAll(User.class);
	     log.debug("Number of users = : " + users.size());

	     mongoOps.dropCollection(User.class);
	}
	private void clearDB() throws DatabaseException{
		
		log.debug("Deleting all RecipesInstance...");
		try {
			recipeService.deleteAllRecipesInstance();
		} catch (DatabaseException de) {
			log.debug("Deleting all RecipesInstance...Error! DatabaseException: " + de.getMessage());
			throw de;
		}
		log.debug("Deleting all RecipesInstance...done!");
		
		log.debug("Deleting all RecipesStruct...");
		try {
			recipeService.deleteAllRecipesStruct();
		} catch (DatabaseException de) {
			log.debug("Deleting all RecipesStruct...Error! DatabaseException: " + de.getMessage());
			throw de;
		}
		log.debug("Deleting all RecipesStruct...done!");
		
		log.debug("Deleting all collectionsChannel...");
		try {
			channelService.deleteAllCollectionChannel();
		} catch (DatabaseException de) {
			log.debug("Deleting all collectionsChannel...Error! DatabaseException: " + de.getMessage());
			throw de;
		}
		log.debug("Deleting all collectionsChannel...done!");
		
		log.debug("Deleting all users...");
		try {
			userService.deleteAllUsers();
		} catch (DatabaseException de) {
			log.debug("Deleting all users...Error! DatabaseException: " + de.getMessage());
			throw de;
		}
		log.debug("Deleting all users...done!");
		
	}
	private void addUsers() throws DatabaseException{
				
		List<User> users = new ArrayList<User>();
		users.add(new User("giuseppe", "piscopo"));
		users.add(new User("davide", "pezzolla"));
		users.add(new User("fabio", "salvini"));
		
		for(User user : users){
			try{
				log.debug("Adding user " + user.getUsername() + "...");
				userService.addUser(user);
			}catch(DuplicateKeyException dke){
				log.debug("Adding user " + user.getUsername() + "...Error! DuplicateKeyException: " + dke.getMessage());
			}catch(DatabaseException de){
				log.debug("Adding user " + user.getUsername() + "...Error! DatabaseException: " + de.getMessage());
			}
			log.debug("Adding user " + user.getUsername() + "...added with id " + user.getId());
		}	
		
	}
	private void addCollectionsChannel() throws DatabaseException{
			
		log.debug("Adding CollectionChannelGMAIL...");
		try{
			addCollectionChannelGMAIL();
			addCollectionChannelGCALENDAR();
		}catch(DatabaseException de){
			log.debug("Adding CollectionChannelGMAIL...Error! DatabaseException: " + de.getMessage());
			throw de;
		}
		log.debug("Adding CollectionChannelGMAIL...Done!");
		
	}
	private void addCollectionChannelGMAIL() throws DatabaseException{
		
		Channel channel = new Channel("GMAIL");
	    
		List<Ingredient> ingredients = new ArrayList<Ingredient>();
	    ingredients.add(new Ingredient("FROM"));
	    ingredients.add(new Ingredient("TO"));
	    ingredients.add(new Ingredient("CC"));
	    ingredients.add(new Ingredient("SUBJECT"));
	    ingredients.add(new Ingredient("BODY"));
	    ingredients.add(new Ingredient("DATE"));
	    
	    List<Trigger> triggers = new ArrayList<Trigger>();
	    triggers.add(new Trigger(channel, "EMAIL_RECEIVED", ingredients));
	    
	    List<Action> actions = new ArrayList<Action>();
	    actions.add(new Action(channel, "SEND_EMAIL", ingredients));
	    
		channelService.addCollectionChannel(channel, triggers, actions);
	    		
	}
	private void addCollectionChannelGCALENDAR() throws DatabaseException{
		
		Channel channel = new Channel("GCALENDAR");
	    
		List<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("SUMMARY"));
		ingredients.add(new Ingredient("DESCRIPTION"));
	    ingredients.add(new Ingredient("LOCATION"));
	    ingredients.add(new Ingredient("CREATOR"));
	    ingredients.add(new Ingredient("CREATED_DATE"));
	    ingredients.add(new Ingredient("ATTENDEES"));
	    ingredients.add(new Ingredient("START_DATE"));
	    ingredients.add(new Ingredient("END_DATE"));
	    
	    List<Trigger> triggers = new ArrayList<Trigger>();
	    triggers.add(new Trigger(channel, "CALENDAR_EVENT_CREATED", ingredients));
	    triggers.add(new Trigger(channel, "CALENDAR_EVENT_STARTED", ingredients));
	    
	    List<Action> actions = new ArrayList<Action>();
	    actions.add(new Action(channel, "CALENDAR_CREATE_EVENT", ingredients));
	    
		channelService.addCollectionChannel(channel, triggers, actions);
	    		
	}
	private void addRecipesStruct() throws DatabaseException, IllegalArgumentException{
		
		log.debug("Adding recipeStruct...");
		User user = userService.getUserByUsername("giuseppe");
		
		RecipeStruct recipeStruct = new RecipeStruct();
		recipeStruct.setAuthor(user);
		recipeStruct.setDescription("if a new event is created, send me a mail");
		recipeStruct.setPublic(false);
		recipeStruct.setTrigger(channelService.getTriggerByName("CALENDAR_EVENT_CREATED"));
		recipeStruct.setAction(channelService.getActionlByName("SEND_EMAIL"));
		
		recipeService.saveRecipeStruct(recipeStruct);
		log.debug("Adding recipeStruct...added with id " + recipeStruct.getId());
				
	}
	private void addRecipesInstance() throws DatabaseException, IllegalArgumentException{
			
		log.debug("Adding recipeInstance...");
		User user = userService.getUserByUsername("giuseppe");
		List<RecipeStruct> recipeStructList = recipeService.getAllRecipesStruct();
			
		RecipeInstance recipeInstance = new RecipeInstance();
		recipeInstance.setUser(user);
		recipeInstance.setActive(true);
		recipeInstance.setLastRefresh(new Date());
		recipeInstance.setRecipeStruct(recipeStructList.get(0));
		
		List<Ingredient> triggerIngredients = new ArrayList<Ingredient>();
		triggerIngredients.add(new Ingredient("LOCATION", "politecnico"));
		
		List<Ingredient> actionIngredients = new ArrayList<Ingredient>();
		actionIngredients.add(new Ingredient("SENDER", "giovanni.malnati@polito.it"));
		actionIngredients.add(new Ingredient("SUBJECT", "new event added description: @DESCRIPTION , location: @LOCATION"));
		actionIngredients.add(new Ingredient("BODY", "event created by @CREATOR . See you soon."));
		
		recipeInstance.setTriggerIngredients(triggerIngredients);
		recipeInstance.setActionIngredients(actionIngredients);

		recipeService.saveRecipeInstance(recipeInstance);
		log.debug("Adding recipeInstance...added with id " + recipeInstance.getId());
		recipeService.activeRecipeInstance(recipeInstance.getId());
		log.debug("RecipeInstance " + recipeInstance.getId() + " activated!");
		
	}

	

}
