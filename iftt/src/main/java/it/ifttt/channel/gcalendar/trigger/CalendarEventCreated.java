package it.ifttt.channel.gcalendar.trigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.api.services.gmail.model.Message;

import it.ifttt.channel.TriggerEvent;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.User;

@Component
public class CalendarEventCreated implements TriggerEvent {

	private final static Logger log = Logger.getLogger(CalendarEventCreated.class);

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
		List<Object> objects = new ArrayList<Object>();
		Map<String, String> event = new HashMap<String, String>();
		event.put("LOCATION", "politecnico");
		event.put("DESCRIPTION", "festa open day");
		event.put("CREATOR", "lambichele");
		event.put("START_DATE", new Date().toString());
		objects.add((Object)event);
		return objects;
	}

	@Override
	public List<Ingredient> injectIngredients(List<Ingredient> injeactableIngredient, Object obj) {
		List<Ingredient> injectedIngredients = new ArrayList<Ingredient>();
		
		Map<String, String> event = (Map<String, String>)obj;
		
		for(Ingredient ingr : injeactableIngredient){
			switch(ingr.getName()){
			case "DESCRIPTION": 
				injectedIngredients.add(new Ingredient(ingr.getName(), event.get("DESCRIPTION")));
				break;
			case "LOCATION": 
				injectedIngredients.add(new Ingredient(ingr.getName(), event.get("LOCATION")));
				break;
			case "CREATOR": 
				injectedIngredients.add(new Ingredient(ingr.getName(), event.get("CREATOR")));
				break;
			case "START_DATE": 
				injectedIngredients.add(new Ingredient(ingr.getName(), event.get("START_DATE")));
				break;
			default:
			}
		}
		
		/*Message message = (Message)obj;
		
		for(Ingredient ingr : injeactableIngredient){
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
