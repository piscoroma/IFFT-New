package it.ifttt.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.ifttt.domain.Action;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.RecipeInstance;
import it.ifttt.domain.Trigger;
import it.ifttt.domain.User;
import it.ifttt.services.RecipeService;

@Component
public class Scheduler {

	private final static Logger log = Logger.getLogger(Scheduler.class);
	
	@Autowired
	private RecipeService recipeService;
	@Autowired
	private TriggerHandler triggerHandler;
	@Autowired
	private ActionHandler actionHandler;
	
		
	@Scheduled(fixedRate=5000)
	public void triggerCheckEvent(){
		
		log.debug("task activated!");
		List<RecipeInstance> recipeInstanceList = null;
		Date lastRefresh = null;
		User user = null;
		Trigger trigger = null;
		Action action = null;
		List<Ingredient> userTriggerIngredients = new ArrayList<Ingredient>();
		List<Ingredient> userActionIngredients = new ArrayList<Ingredient>();
		List<Ingredient> injectableIngredients = new ArrayList<Ingredient>();
		List<Ingredient> injectedIngredients = new ArrayList<Ingredient>();
		
		recipeInstanceList = recipeService.getAllActiveRecipesInstance();
		log.debug("There are " + recipeInstanceList.size() + " recipes to verify");
		for(RecipeInstance recipeInstance : recipeInstanceList){			
			log.debug("Verifing recipe...");
			lastRefresh = recipeInstance.getLastRefresh();
			user = recipeInstance.getUser();
			trigger = recipeInstance.getRecipeStruct().getTrigger();
			userTriggerIngredients = recipeInstance.getTriggerIngredients();
			try{
				triggerHandler.initialize(trigger);
				List<Object> triggerParams = triggerHandler.raise(user, userTriggerIngredients, lastRefresh);
				recipeService.updateRefreshTime(recipeInstance.getId());
				if(triggerParams.size() > 0){
					log.debug("Trigger condition is satisfied, so trying to perform the action...");
					action = recipeInstance.getRecipeStruct().getAction();
					userActionIngredients = recipeInstance.getActionIngredients();
					injectableIngredients = recipeInstance.getRecipeStruct().getTrigger().getIngredients();
					actionHandler.initialize(action);
					for(Object params : triggerParams){
						injectedIngredients = triggerHandler.injectIngredients(injectableIngredients, params);
						actionHandler.setInjectableIngredients(injectedIngredients);
						actionHandler.perform(user, userActionIngredients);
					}
				}else
					log.debug("Trigger condition is not satisfied");
			}catch(Exception e){
				log.debug("Verifing recipe...Exception: " + e.getMessage());
			}
			log.debug("Verifing recipe...done!");
		}
		log.debug("All recipes has been verified");
	
	}

}
