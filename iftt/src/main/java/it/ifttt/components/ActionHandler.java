package it.ifttt.components;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.ifttt.channel.ActionPerformer;
import it.ifttt.domain.Action;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.User;

@Component
public class ActionHandler {
	
	private Action action;
	private ActionPerformer actionPerformer;
	
	@Autowired
	private ChannelManager channelManager;
	
	
	public void initialize(Action action){
		this.action = action;
		this.actionPerformer = setAction(action);
	}
	
	/*public void setInjectableIngredients(List<Ingredient> injectableIngredients){
		actionPerformer.setInjectedIngredients(injectableIngredients);
	}*/
	
	public void perform(User user, List<Ingredient> ingredients) throws Exception{
		try{
			actionPerformer.setUser(user);
			actionPerformer.setUserIngredients(ingredients);
			actionPerformer.perform();
		}catch(Exception e){
			throw e;
		}
	}
	
	private ActionPerformer setAction(Action action){
		String channelName = action.getChannel().getName();		
		return channelManager.setActionPerformer(channelName, action.getName());
	}
	
	
}
