package it.ifttt.channel.gmail.trigger;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import it.ifttt.channel.TriggerEvent;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.User;

@Component
public class MailReceivedEvent implements TriggerEvent {

	@Override
	public void setUser(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLastRefresh(Date lastRefresh) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserIngredients(List<Ingredient> ingredients) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Object> raise() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Ingredient> injectIngredients(List<Ingredient> injeactableIngredient, Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
