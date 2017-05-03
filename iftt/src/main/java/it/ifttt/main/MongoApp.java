package it.ifttt.main;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.dao.DuplicateKeyException;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Update.update;
import static org.springframework.data.mongodb.core.query.Query.query;

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

/*@Configuration
@ComponentScan(basePackages={"it.ifttt"})
@EnableAutoConfiguration
@EnableScheduling*/
public class MongoApp implements CommandLineRunner{

	private static Logger log = LogManager.getLogger();

	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
    UserService userService;
	@Autowired
	ChannelService channelService;
	@Autowired
    RecipeService recipeService;
	
	
	public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MongoApp.class, args);
	}

	public void run(String... arg0) throws Exception {
		
		log.info("Hello World!");
		
		//testQuery();
		try{
			clearDB();
			addUsers();
			addCollectionsChannel();
			addRecipesStruct();
			addRecipesInstance();
		}catch(DatabaseException | IllegalArgumentException e){
			log.info("Exception: " + e.getMessage());
			return; 
		}
		
	}
	
	private void testQuery(){
		
		MongoOperations mongoOps = mongoTemplate;

		User user = new User("giuseppe", "piscopo", "user");
 		
		mongoOps.dropCollection(User.class);
		
	    // Insert is used to initially store the object into the database.
	 	mongoOps.insert(user);
	 	log.info("insert user: " + user);

	     // Find
	 	user = mongoOps.findById(user.getId(), User.class);
	 	log.info("found: " + user);

	     // Update
	 	mongoOps.updateFirst(query(where("username").is("giuseppe")), update("password","PISCOPO"), User.class);
	 	user = mongoOps.findOne(query(where("username").is("giuseppe")), User.class);
	 	log.info("Updated: " + user);
	 	
	 	// Delete
	     mongoOps.remove(user);

	     // Check that deletion worked
	     List<User> users =  mongoOps.findAll(User.class);
	     log.info("Number of users = : " + users.size());

	     mongoOps.dropCollection(User.class);
	}
	private void clearDB() throws DatabaseException{
		
		log.info("Deleting all RecipesInstance...");
		try {
			recipeService.deleteAllRecipesInstance();
		} catch (DatabaseException de) {
			log.info("Deleting all RecipesInstance...Error! DatabaseException: " + de.getMessage());
			throw de;
		}
		log.info("Deleting all RecipesInstance...done!");
		
		log.info("Deleting all RecipesStruct...");
		try {
			recipeService.deleteAllRecipesStruct();
		} catch (DatabaseException de) {
			log.info("Deleting all RecipesStruct...Error! DatabaseException: " + de.getMessage());
			throw de;
		}
		log.info("Deleting all RecipesStruct...done!");
		
		log.info("Deleting all collectionsChannel...");
		try {
			channelService.deleteAllCollectionChannel();
		} catch (DatabaseException de) {
			log.info("Deleting all collectionsChannel...Error! DatabaseException: " + de.getMessage());
			throw de;
		}
		log.info("Deleting all collectionsChannel...done!");
		
		log.info("Deleting all users...");
		try {
			userService.deleteAllUsers();
		} catch (DatabaseException de) {
			log.info("Deleting all users...Error! DatabaseException: " + de.getMessage());
			throw de;
		}
		log.info("Deleting all users...done!");
		
	}
	private void addUsers() throws DatabaseException{
				
		List<User> users = new ArrayList<User>();
		users.add(new User("giuseppe", "piscopo", "user"));
		users.add(new User("davide", "pezzolla", "user"));
		users.add(new User("fabio", "salvini", "user"));
		
		for(User user : users){
			try{
				log.info("Adding user " + user.getUsername() + "...");
				userService.addUser(user);
			}catch(DuplicateKeyException dke){
				log.info("Adding user " + user.getUsername() + "...Error! DuplicateKeyException: " + dke.getMessage());
			}catch(DatabaseException de){
				log.info("Adding user " + user.getUsername() + "...Error! DatabaseException: " + de.getMessage());
			}
			log.info("Adding user " + user.getUsername() + "...added with id " + user.getId());
		}	
		
	}
	private void addCollectionsChannel() throws DatabaseException{
			
		log.info("Adding CollectionChannelGMAIL...");
		try{
			addCollectionChannelGMAIL();
			addCollectionChannelGCALENDAR();
			addCollectionChannelTWITTER();
		}catch(DatabaseException de){
			log.info("Adding CollectionChannelGMAIL...Error! DatabaseException: " + de.getMessage());
			throw de;
		}
		log.info("Adding CollectionChannelGMAIL...Done!");
		
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
	    triggers.add(new Trigger(channel, "EMAIL_RECEIVED", triggerIngredients, injectableIngredients));
	    
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
	    triggers.add(new Trigger(channel, "CALENDAR_EVENT_CREATED", triggerIngredients, injectableIngredients));
	    triggers.add(new Trigger(channel, "CALENDAR_EVENT_STARTED", triggerIngredients, injectableIngredients));
	    
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
		triggerIngredients.add(new Ingredient("FROM"));
		triggerIngredients.add(new Ingredient("TEXT"));
		
		List<Ingredient> injectableIngredients = new ArrayList<Ingredient>(triggerIngredients);
		injectableIngredients.add(new Ingredient("TWEET_ID"));
		injectableIngredients.add(new Ingredient("NLIKES"));
		injectableIngredients.add(new Ingredient("DATE"));
		injectableIngredients.add(new Ingredient("REPLY_TO_STATUS_ID"));
		
		List<Trigger> triggers = new ArrayList<Trigger>();
		triggers.add(new Trigger(channel, "NEW_TWEET_EVENT", triggerIngredients, injectableIngredients));
		
		List<Ingredient> actionIngredients = new ArrayList<Ingredient>();
		actionIngredients.add(new Ingredient("TEXT"));
		actionIngredients.add(new Ingredient("REPLY_TO_STATUS_ID"));
		
		List<Action> actions = new ArrayList<Action>();
		actions.add(new Action(channel, "TWEET_STATE_ACTION", actionIngredients));
		
		channelService.addCollectionChannel(channel, triggers, actions);
		
	}
	private void addRecipesStruct() throws DatabaseException, IllegalArgumentException{
		
		log.info("Adding recipeStruct...");
		User user = userService.getUserByUsername("giuseppe");
		
		RecipeStruct recipeStruct = new RecipeStruct();
		recipeStruct.setAuthor(user);
		recipeStruct.setDescription("if receive a email from a particular sender, send me a mail");
		recipeStruct.setPublic(false);
		recipeStruct.setTrigger(channelService.getTriggerByName("EMAIL_RECEIVED"));
		recipeStruct.setAction(channelService.getActionlByName("SEND_EMAIL"));
		
		recipeService.saveRecipeStruct(recipeStruct);
		log.info("Adding recipeStruct...added with id " + recipeStruct.getId());
				
	}
	private void addRecipesInstance() throws DatabaseException, IllegalArgumentException{
			
		log.info("Adding recipeInstance...");
		User user = userService.getUserByUsername("giuseppe");
		List<RecipeStruct> recipeStructList = recipeService.getAllRecipesStruct();
			
		RecipeInstance recipeInstance = new RecipeInstance();
		recipeInstance.setUser(user);
		recipeInstance.setActive(true);
		recipeInstance.setLastRefresh(new Date());
		recipeInstance.setRecipeStruct(recipeStructList.get(0));
		
		List<Ingredient> triggerIngredients = new ArrayList<Ingredient>();
		triggerIngredients.add(new Ingredient("RECEIVER", "fulvio.risso@polito.it"));
		
		List<Ingredient> actionIngredients = new ArrayList<Ingredient>();
		actionIngredients.add(new Ingredient("SENDER", "giovanni.malnati@polito.it"));
		actionIngredients.add(new Ingredient("SUBJECT", "alert"));
		
		recipeInstance.setTriggerIngredients(triggerIngredients);
		recipeInstance.setActionIngredients(actionIngredients);

		recipeService.saveRecipeInstance(recipeInstance);
		log.info("Adding recipeInstance...added with id " + recipeInstance.getId());
		recipeService.activeRecipeInstance(recipeInstance.getId());
		log.info("RecipeInstance " + recipeInstance.getId() + " activated!");
		
	}
}


