package it.ifttt.components;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.ifttt.channel.TriggerEvent;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.Trigger;
import it.ifttt.domain.User;

@Component
public class TriggerHandler {
	
	private Trigger trigger;
	private TriggerEvent event;	
	
	@Autowired
	private ChannelManager channelManager;
	
	
	public void initialize(Trigger trigger){
		this.trigger = trigger;
		this.event = setEvent(trigger);
	}
	
	public List<Object> raise(User user, List<Ingredient> ingredients, Date lastRefresh) throws Exception{
		try{
			event.setUser(user);
			event.setUserIngredients(ingredients);	
			event.setLastRefresh(lastRefresh);
			return event.raise();
		}catch(Exception e){
			throw e;
		}
	}
	
	public Date getLastRefresh(){
		return event.getLastRefresh();
	}
	
	public List<Ingredient> injectIngredients(List<Ingredient> injeactableIngredient, Object obj){
		return event.injectIngredients(injeactableIngredient, obj);
	}
	
	private TriggerEvent setEvent(Trigger trigger){
		String channelName = trigger.getChannel().getName();		
		return channelManager.setTriggerEvent(channelName, trigger.getName());
	}


}
