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

	/*
	 * Trigger: event-starts
	 * 
	 * Ingredienti suportati:
	 * - SUMMARY: il trigger si scatena solo se il titolo dell'evento coincide
	 * - DESCRIPTION: il trigger si scatena solo se la descrizione CONTIENE questo testo
	 * - LOCATION: il trigger si scatena solo se la location è la stessa
	 * - CREATOR: il trigger si scatena solo se l'email dell'organizzatore coincide con questa
	 * 
	 * L'evento che si genera se il trigger è verificato contiene i seguenti elementi:
	 * - SUMMARY: titolo dell'evento
	 * - DESCRIPTION: descrizione dell'evento
	 * - LOCATION: luogo dell'evento
	 * - CREATOR: organizzatore dell'evento
	 * - ATTENDEES: lista delle email degli invitati
	 * - CREATED_DATE: data di creazione dell'evento
	 * - START_DATE: data di inizio evento (se l'evento è tutto il giorno, l'orario sarà 00:00:00)
	 * - END_DATE: data di fine evento (se l'evento è tutto il giorno, sarà un giorno dopo di start-date, sempre 00:00:00)
	 * 
	 */
	
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
	public Date getLastRefresh() {
		return lastRefresh;
	}

	@Override
	public void setUserIngredients(List<Ingredient> ingredients) {
		this.userIngredients = ingredients;
	}
	
	@Override
	public List<Object> raise() {
		List<Object> events = new ArrayList<Object>();
		log.debug("TRIGGER: i'm CalendarEventStarted");
		log.debug("user: " + user.toString());
		log.debug(lastRefresh.toString());
		log.debug(userIngredients.toString());
		this.lastRefresh = new Date();
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
