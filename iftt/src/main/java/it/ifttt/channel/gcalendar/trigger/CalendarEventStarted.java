package it.ifttt.channel.gcalendar.trigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.api.services.gmail.model.Message;

import it.ifttt.channel.TriggerEvent;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.User;

@Component
public class CalendarEventStarted implements TriggerEvent {

	private final static Logger log = Logger.getLogger(CalendarEventStarted.class);

	private User user;
	private Date lastRefresh;
	private List<Ingredient> userIngredients;
	
	@Override
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public void setLastRefresh(Date lastRefresh) {
		this.lastRefresh = lastRefresh;
	}

	@Override
	public void setUserIngredients(List<Ingredient> ingredients) {
		this.userIngredients = ingredients;
	}
	
	@Override
	public List<Object> raise() {
		log.debug("TRIGGER: i'm CalendarEventStarted");
		log.debug("user: " + user.toString());
		log.debug(lastRefresh.toString());
		log.debug(userIngredients.toString());
		return null;
	}

	@Override
	public List<Ingredient> injectIngredients(List<Ingredient> injeactableIngredient, Object obj) {
		List<Ingredient> injectedIngredients = new ArrayList<Ingredient>();
		Message message = (Message)obj;
		
		/*for(Ingredient ingr : injeactableIngredient){
			switch(ingr.getName()){
			case "SUMMARY": 
				injectedIngredients.add(new Ingredient(ingr.getName(), (String) message.get(SUMMARY_KEY));
				break;
			case "DESCRIPTION": 
				injectedIngredients.add(new Ingredient(ingr.getName(), (String) message.get(DESCRIPTION_KEY));
				break;
			case "LOCATION": 
				injectedIngredients.add(new Ingredient(ingr.getName(), (String) message.get(LOCATION_KEY));
				break;
			case "CREATOR": 
				injectedIngredients.add(new Ingredient(ingr.getName(), (String) message.get(CREATOR_KEY));
				break;
			case "CREATED_DATE": 
				injectedIngredients.add(new Ingredient(ingr.getName(), (String) message.get(CREATED_DATE_KEY));
				break;
			case "ATTENDEES": 
				injectedIngredients.add(new Ingredient(ingr.getName(), (String) message.get(ATTENDEES_KEY));
				break;
			case "START_DATE": 
				injectedIngredients.add(new Ingredient(ingr.getName(), (String) message.get(START_DATE_KEY));
				break;
			case "END_DATE": 
				injectedIngredients.add(new Ingredient(ingr.getName(), (String) message.get(END_DATE_KEY));
				break;
			default:
			}
		}*/
		
		return injectedIngredients;
	}

	

}
