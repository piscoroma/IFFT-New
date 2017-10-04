package it.ifttt.channel.gcalendar.trigger;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Event.Creator;
import com.google.api.services.calendar.model.Events;

import it.ifttt.channel.TriggerEvent;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.User;
import it.ifttt.exceptions.UnauthorizedChannelException;
import it.ifttt.social_api_creators.GcalendarCreator;

@Component
public class CalendarEventCreated implements TriggerEvent {

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
	 * - CREATOR_NAME: nome e cognome google del creatore
	 * - CREATED_DATE: data di creazione dell'evento
	 * - START_DATE: data di inizio evento (se l'evento è tutto il giorno, l'orario sarà 00:00:00)
	 * - END_DATE: data di fine evento (se l'evento è tutto il giorno, sarà un giorno dopo di start-date, sempre 00:00:00)
	 * 
	 */
	
	public static final String SUMMARY_KEY = "summary";
	public static final String DESCRIPTION_KEY = "description";
	public static final String LOCATION_KEY = "location";
	public static final String CREATOR_KEY = "creator";
	
	public static final String ATTENDEES_KEY = "attendees";
	public static final String CREATOR_NAME_KEY = "creator-name";
	public static final String CREATED_DATE_KEY = "created";
	public static final String START_DATE_KEY = "start";
	public static final String END_DATE_KEY = "end";
	
	@Autowired
	private GcalendarCreator gCalendarCreator;
	
	private final static Logger log = Logger.getLogger(CalendarEventCreated.class);

	private User user;
	private Date lastRefresh;
	private Map<String, String> userIngredients;
	
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
		userIngredients = new HashMap<String, String>();
		for(Ingredient ingr : ingredients)
			userIngredients.put(ingr.getName(), ingr.getValue());
	}
	
	@Override
	public List<Object> raise() throws UnauthorizedChannelException, GeneralSecurityException, IOException {

		List<Object> events = new ArrayList<Object>();
		List<Event> eventSatisfiedList = new ArrayList<Event>();
		log.debug("-->richiesta a gcalendar");
		eventSatisfiedList = getNextNewEvent();
		for(Event eventSatisfied : eventSatisfiedList )
			events.add((Object)eventSatisfied);
		return events;
		
		/*List<Object> events = new ArrayList<Object>();
		Map<String, String> event = new HashMap<String, String>();
		event.put(LOCATION_KEY, "politecnico");
		event.put(DESCRIPTION_KEY, "festa open day");
		event.put(CREATOR_KEY, "lambichele");
		event.put(START_DATE_KEY, new Date().toString());
		events.add((Object)event);
		this.lastRefresh = new Date();
		return events;*/
	}

	@Override
	public List<Ingredient> injectIngredients(List<Ingredient> injeactableIngredient, Object obj) {
		
		List<Ingredient> injectedIngredients = new ArrayList<Ingredient>();
		Event event = (Event)obj;
		
		for(Ingredient ingr : injeactableIngredient){
			try {
				switch(ingr.getName()){
				case SUMMARY_KEY: 
					injectedIngredients.add(new Ingredient(ingr.getName(), ingr.getKey(), (String) event.get(SUMMARY_KEY)));
					break;
				case DESCRIPTION_KEY: 
					injectedIngredients.add(new Ingredient(ingr.getName(), ingr.getKey(), (String) event.get(DESCRIPTION_KEY)));
					break;
				case LOCATION_KEY: 
					injectedIngredients.add(new Ingredient(ingr.getName(), ingr.getKey(), (String) event.get(LOCATION_KEY)));
					break;
				case ATTENDEES_KEY: 
					injectedIngredients.add(new Ingredient(ingr.getName(), ingr.getKey(), (String) event.get(ATTENDEES_KEY)));
					break;
				case CREATED_DATE_KEY: 
					injectedIngredients.add(new Ingredient(ingr.getName(), ingr.getKey(), event.get(CREATED_DATE_KEY).toString()));
					break;
				case CREATOR_NAME_KEY: 
					injectedIngredients.add(new Ingredient(ingr.getName(), ingr.getKey(), (String) ((Creator) event.get(CREATOR_KEY)).getDisplayName()));
					break;
				case CREATOR_KEY: 
					injectedIngredients.add(new Ingredient(ingr.getName(), ingr.getKey(), (String) ((Creator) event.get(CREATOR_KEY)).getEmail()));
					break;
				case START_DATE_KEY: 
					injectedIngredients.add(new Ingredient(ingr.getName(), ingr.getKey(), event.get(START_DATE_KEY).toString()));
					break;
				case END_DATE_KEY: 
					injectedIngredients.add(new Ingredient(ingr.getName(), ingr.getKey(), event.get(END_DATE_KEY).toString()));
					break;
				default:
				}
			}catch(Exception e){
				log.error(e.getMessage());
			}
		}
		return injectedIngredients;
	}

	List<Event> getNextNewEvent() throws UnauthorizedChannelException, GeneralSecurityException, IOException {

		// Get calendar API for user
		Calendar calendar = gCalendarCreator.getCalendar(user.getUsername());
		
		// get last created events
		DateTime now = new DateTime(System.currentTimeMillis());
        Events events = calendar.events().list("primary")
            .setTimeMin(now)
            .setOrderBy("updated")
            .setSingleEvents(false)
            .execute();
        List<Event> items = events.getItems();
        if (items.size() == 0)
            return null;
        
        List<Event> eventSatisfied = new ArrayList<Event>();
        for (Event event : items) {
        	
        	System.out.printf("Event: %s (%s)\n", event.getSummary(), event.getCreated());
        	
        	if (this.lastRefresh==null || new Date(event.getCreated().getValue()).after(this.lastRefresh)) {
        		System.out.println("Is new event");
        		this.lastRefresh = new Date(event.getCreated().getValue());
        		if (eventSatisfyTrigger(event)) {
        			System.out.println("Event satisfy trigger");
        			eventSatisfied.add(event);
        		}
        	}
        }
		return eventSatisfied;
	}
	
	private boolean eventSatisfyTrigger(Event event) {
		
		if (userIngredients.containsKey(SUMMARY_KEY) && (event.getSummary() == null
				|| !userIngredients.get(SUMMARY_KEY).equals(event.getSummary())))
			return false;
		if (userIngredients.containsKey(DESCRIPTION_KEY) && (event.getDescription() == null
				|| !((String) event.getDescription()).toLowerCase().contains(userIngredients.get(DESCRIPTION_KEY).toLowerCase())))
			return false;
		if (userIngredients.containsKey(LOCATION_KEY) && (event.getLocation() == null
				|| !((String) event.getLocation()).toLowerCase().contains(userIngredients.get(LOCATION_KEY).toLowerCase())))
			return false;
		if (userIngredients.containsKey(CREATOR_KEY) && (event.getCreator() == null
				|| !userIngredients.get(CREATOR_KEY).equals(event.getCreator().getEmail())))
			return false;
		
		return true;
	}



}
