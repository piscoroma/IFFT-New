package it.ifttt.components;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

import java.util.ArrayList;
import java.util.Arrays;
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
			//addRecipesInstance();
		}catch(DatabaseException | IllegalArgumentException e){
			log.debug("Exception: " + e.getMessage());
		}
		dbLoad = true;
		
	}
	
	private void testQuery(){
		
		MongoOperations mongoOps = mongoTemplate;

		User user = new User("giuseppe", "piscopo", "user");
 		
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
		users.add(new User("giuseppe", "piscopo", "user"));
		users.add(new User("davide", "pezzolla", "user"));
		users.add(new User("fabio", "salvini", "user"));
		
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
			addCollectionChannelTWITTER();
			addCollectionChannelWEATHER();
		}catch(DatabaseException de){
			log.debug("Adding CollectionChannel...Error! DatabaseException: " + de.getMessage());
			throw de;
		}
		log.debug("Adding CollectionChannelGMAIL...Done!");
		
	}
	private void addCollectionChannelGMAIL() throws DatabaseException{
		
		Channel channel = new Channel("GMAIL");
	    
		List<Ingredient> triggerIngredients = new ArrayList<Ingredient>();
		triggerIngredients.add(new Ingredient("FROM"));
	    triggerIngredients.add(new Ingredient("SUBJECT"));
	    triggerIngredients.add(new Ingredient("BODY"));
	    triggerIngredients.add(new Ingredient("DATE"));
	    
	    List<Ingredient> injectableIngredients = new ArrayList<Ingredient>(triggerIngredients);   
	    injectableIngredients.add(new Ingredient("CC"));
	    
	    List<Trigger> triggers = new ArrayList<Trigger>();
	    triggers.add(new Trigger(channel, "EMAIL_RECEIVED", "Email received", triggerIngredients, injectableIngredients));
	    
	    List<Ingredient> actionIngredients = new ArrayList<Ingredient>();
	    actionIngredients.add(new Ingredient("TO"));
	    actionIngredients.add(new Ingredient("CC"));
	    actionIngredients.add(new Ingredient("SUBJECT"));
	    actionIngredients.add(new Ingredient("BODY"));
	    
	    List<Action> actions = new ArrayList<Action>();
	    actions.add(new Action(channel, "SEND_EMAIL", actionIngredients));
	    
		channelService.addCollectionChannel(channel, triggers, actions);
	    		
	}
	private void addCollectionChannelGCALENDAR() throws DatabaseException{
		
		Channel channel = new Channel("GCALENDAR");
	    
		List<Ingredient> triggerIngredients = new ArrayList<Ingredient>();
		triggerIngredients.add(new Ingredient("SUMMARY"));
		triggerIngredients.add(new Ingredient("DESCRIPTION"));
		triggerIngredients.add(new Ingredient("LOCATION"));
		triggerIngredients.add(new Ingredient("CREATOR"));
	    
	    List<Ingredient> injectableIngredients = new ArrayList<Ingredient>(triggerIngredients);   
	    injectableIngredients.add(new Ingredient("CREATED_DATE"));
	    injectableIngredients.add(new Ingredient("ATTENDEES"));
	    injectableIngredients.add(new Ingredient("START_DATE"));
	    injectableIngredients.add(new Ingredient("END_DATE"));
	    
	    List<Trigger> triggers = new ArrayList<Trigger>();
	    triggers.add(new Trigger(channel, "CALENDAR_EVENT_CREATED", "Calendar event created", triggerIngredients, injectableIngredients));
	    triggers.add(new Trigger(channel, "CALENDAR_EVENT_STARTED", "Calendar event started", triggerIngredients, injectableIngredients));
	    
	    List<Ingredient> actionIngredients = new ArrayList<Ingredient>(injectableIngredients);
	    actionIngredients.add(new Ingredient("ALL_DAY"));
	    actionIngredients.add(new Ingredient("TIMEZONE"));
	    
	    List<Action> actions = new ArrayList<Action>();
	    actions.add(new Action(channel, "CALENDAR_CREATE_EVENT", actionIngredients));
	    
		channelService.addCollectionChannel(channel, triggers, actions);
	    		
	}
	private void addCollectionChannelTWITTER() throws DatabaseException{
		
		Channel channel = new Channel("TWITTER");
		
		List<Ingredient> triggerIngredients = new ArrayList<Ingredient>();
		triggerIngredients.add(new Ingredient("FROM", false, "", "text", null));
		triggerIngredients.add(new Ingredient("TEXT", false, "", "text", null));
		
		List<Ingredient> injectableIngredients = new ArrayList<Ingredient>(triggerIngredients);
		injectableIngredients.add(new Ingredient("TWEET_ID"));
		injectableIngredients.add(new Ingredient("NLIKES"));
		injectableIngredients.add(new Ingredient("DATE"));
		injectableIngredients.add(new Ingredient("REPLY_TO_STATUS_ID"));
		
		List<Trigger> triggers = new ArrayList<Trigger>();
		triggers.add(new Trigger(channel, "NEW_TWEET_EVENT", "New tweet event", triggerIngredients, injectableIngredients));
		
		List<Ingredient> actionIngredients = new ArrayList<Ingredient>();
		actionIngredients.add(new Ingredient("TEXT"));
		actionIngredients.add(new Ingredient("REPLY_TO_STATUS_ID"));
		
		List<Action> actions = new ArrayList<Action>();
		actions.add(new Action(channel, "TWEET_STATE_ACTION", actionIngredients));
		
		channelService.addCollectionChannel(channel, triggers, actions);
		
	}
	
	private void addCollectionChannelWEATHER() throws DatabaseException{
		
		Channel channel = new Channel("WEATHER");
		
		List<Ingredient> triggerIngredients = new ArrayList<Ingredient>();
		triggerIngredients.add(new Ingredient("condition", false, "", "list", Arrays.asList("Sunny", "Showers", "Rain", "Mostly Sunny", "Partly Cloudy", "Thunderstorms", "Scattered Showers", "Scattered Thunderstorms")));
		triggerIngredients.add(new Ingredient("location", true, "", "location", null));
		triggerIngredients.add(new Ingredient("temperature-high", false, "°C", "number", null));
		triggerIngredients.add(new Ingredient("temperature-low", false, "°C", "number", null));
		triggerIngredients.add(new Ingredient("humidity-high", false, "%", "number", null));
		triggerIngredients.add(new Ingredient("humidity-low", false, "%", "°number", null));
		triggerIngredients.add(new Ingredient("pressure-high", false, "mb millibar", "number", null));
		triggerIngredients.add(new Ingredient("pressure-low", false, "mb millibar", "number", null));
		triggerIngredients.add(new Ingredient("visibility-high", false, "Km", "number", null));
		triggerIngredients.add(new Ingredient("visibility-low", false, "Km", "number", null));
		triggerIngredients.add(new Ingredient("wind-speed-high", false, "Km/h", "text", null));
		triggerIngredients.add(new Ingredient("wind-speed-low", false, "Km/h", "text", null));
		
		List<Ingredient> injectableIngredients = new ArrayList<Ingredient>();
		injectableIngredients.add(new Ingredient("condition"));
		injectableIngredients.add(new Ingredient("location"));
		injectableIngredients.add(new Ingredient("title"));
		injectableIngredients.add(new Ingredient("temperature"));
		injectableIngredients.add(new Ingredient("humidity"));
		injectableIngredients.add(new Ingredient("pressure"));
		injectableIngredients.add(new Ingredient("visibility"));
		injectableIngredients.add(new Ingredient("wind-speed"));
		
		List<Trigger> triggers = new ArrayList<Trigger>();
		triggers.add(new Trigger(channel, "CURRENT_WEATHER_TRIGGER", "Current weather", triggerIngredients, injectableIngredients));
		
		List<Action> actions = new ArrayList<Action>();
		
		channelService.addCollectionChannel(channel, triggers, actions);
		
	}
	private void addRecipesStruct() throws DatabaseException, IllegalArgumentException{
		
		log.debug("Adding recipeStruct...");
		User user = userService.getUserByUsername("giuseppe");
		
		RecipeStruct recipeStruct = new RecipeStruct();
		recipeStruct.setAuthor(user);
		recipeStruct.setDescription("if a new event is created, send me a mail");
		recipeStruct.setPublic(true);
		recipeStruct.setTrigger(channelService.getTriggerByName("CALENDAR_EVENT_CREATED"));
		recipeStruct.setAction(channelService.getActionByName("SEND_EMAIL"));
		
		recipeService.saveRecipeStruct(recipeStruct);
		log.debug("Adding recipeStruct...added with id " + recipeStruct.getId());
		
		/*-------------*/
		
		log.debug("Adding recipeStruct...");
		
		recipeStruct = new RecipeStruct();
		recipeStruct.setAuthor(user);
		recipeStruct.setDescription("if a new event is created, tweet");
		recipeStruct.setPublic(true);
		recipeStruct.setTrigger(channelService.getTriggerByName("CALENDAR_EVENT_CREATED"));
		recipeStruct.setAction(channelService.getActionByName("TWEET_STATE_ACTION"));
		
		recipeService.saveRecipeStruct(recipeStruct);
		log.debug("Adding recipeStruct...added with id " + recipeStruct.getId());
		
		/*-------------*/
		
		log.debug("Adding recipeStruct...");
		
		recipeStruct = new RecipeStruct();
		recipeStruct.setAuthor(user);
		recipeStruct.setDescription("if receive a tweet, send a mail");
		recipeStruct.setPublic(false);
		recipeStruct.setTrigger(channelService.getTriggerByName("NEW_TWEET_EVENT"));
		recipeStruct.setAction(channelService.getActionByName("SEND_EMAIL"));
		
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
		recipeInstance.setLastRefresh(null);
		recipeInstance.setRecipeStruct(recipeStructList.get(0));
		
		List<Ingredient> triggerIngredients = new ArrayList<Ingredient>();
		triggerIngredients.add(new Ingredient("LOCATION", "politecnico"));
		
		List<Ingredient> actionIngredients = new ArrayList<Ingredient>();
		actionIngredients.add(new Ingredient("SENDER", "giaaovanni.malnati@polito.it"));
		actionIngredients.add(new Ingredient("SUBJECT", "new event added description: @DESCRIPTION , location: @LOCATION"));
		actionIngredients.add(new Ingredient("BODY", "event created by @CREATOR . See you soon."));
		
		recipeInstance.setTriggerIngredients(triggerIngredients);
		recipeInstance.setActionIngredients(actionIngredients);

		recipeService.saveRecipeInstance(recipeInstance);
		log.debug("Adding recipeInstance...added with id " + recipeInstance.getId());
		recipeService.activeRecipeInstance(recipeInstance.getId());
		log.debug("RecipeInstance " + recipeInstance.getId() + " activated!");
		
		/*-------------*/
		
		log.debug("Adding recipeInstance...");
			
		recipeInstance = new RecipeInstance();
		recipeInstance.setUser(user);
		recipeInstance.setActive(true);
		recipeInstance.setLastRefresh(null);
		recipeInstance.setRecipeStruct(recipeStructList.get(1));
		
		triggerIngredients = new ArrayList<Ingredient>();
		triggerIngredients.add(new Ingredient("LOCATION", "politecnico"));
		
		actionIngredients = new ArrayList<Ingredient>();
		actionIngredients.add(new Ingredient("TEXT", "tweet: new event added description: @DESCRIPTION , location: @LOCATION"));
		
		recipeInstance.setTriggerIngredients(triggerIngredients);
		recipeInstance.setActionIngredients(actionIngredients);

		recipeService.saveRecipeInstance(recipeInstance);
		log.debug("Adding recipeInstance...added with id " + recipeInstance.getId());
		recipeService.activeRecipeInstance(recipeInstance.getId());
		log.debug("RecipeInstance " + recipeInstance.getId() + " activated!");
		
		/*-------------*/
		
		log.debug("Adding recipeInstance...");
			
		recipeInstance = new RecipeInstance();
		recipeInstance.setUser(user);
		recipeInstance.setActive(true);
		recipeInstance.setLastRefresh(null);
		recipeInstance.setRecipeStruct(recipeStructList.get(2));
		
		triggerIngredients = new ArrayList<Ingredient>();
		triggerIngredients.add(new Ingredient("TEXT", "#JuveGenoa"));
		
		actionIngredients = new ArrayList<Ingredient>();
		actionIngredients.add(new Ingredient("SENDER", "pepe@gmail.it"));
		actionIngredients.add(new Ingredient("SUBJECT", "new tweet received from @FROM"));
		actionIngredients.add(new Ingredient("BODY", "tweet text: @TEXT. nLikes: @NLIKES"));
		
		recipeInstance.setTriggerIngredients(triggerIngredients);
		recipeInstance.setActionIngredients(actionIngredients);

		recipeService.saveRecipeInstance(recipeInstance);
		log.debug("Adding recipeInstance...added with id " + recipeInstance.getId());
		recipeService.activeRecipeInstance(recipeInstance.getId());
		log.debug("RecipeInstance " + recipeInstance.getId() + " activated!");
		
	}

	

}
