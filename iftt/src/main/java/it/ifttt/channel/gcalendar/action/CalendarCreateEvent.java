package it.ifttt.channel.gcalendar.action;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;

import it.ifttt.channel.ActionPerformer;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.User;
import it.ifttt.social_api_creators.GcalendarCreator;

@Component
public class CalendarCreateEvent implements ActionPerformer {

	/*
	 * Action: create-event
	 * 
	 * Ingredienti:
	 * - SUMMARY: titolo dell'evento
	 * - DESCRIPTION: descrizione dell'evento
	 * - LOCATION: luogo dell'evento
	 * - CREATOR: organizzatore dell'evento
	 * - [ATTENDEES]: lista delle email degli invitati separate da spazi
	 * - CREATED_DATE: data di creazione dell'evento
	 * - START_DATE: data di inizio evento (se l'evento è tutto il giorno, l'orario sarà 00:00:00)
	 * - END_DATE: data di fine evento (se l'evento è tutto il giorno, sarà un giorno dopo di start-date, sempre 00:00:00)
	 * - ALL_DAY: (boolean) dice se l'evento è tutto il giorno. in tal caso si usa sempre "start" come data [obbligatorio]
	 * - TIME_ZONE: timezone per le date specificate
	 * 
	 */
	
	@Autowired
	private GcalendarCreator gcalendarCreator;

	private final static Logger log = Logger.getLogger(CalendarCreateEvent.class);

	private User user;
	private Map<String, String> userIngredients;
	
	@Override
	public void setUser(User user) {
		this.user = user;	
	}

	@Override
	public void setUserIngredients(List<Ingredient> userIngredientsList) {
		userIngredients = new HashMap<String, String>();
		for(Ingredient ingr : userIngredientsList)
			userIngredients.put(ingr.getName(), ingr.getValue());	
	}

	@Override
	public void perform() throws Exception{
		log.debug("ACTION: i'm CalendarCreateEvent");
		log.debug("user: " + user.toString());
		log.debug(userIngredients.toString());
		
		String[] attendees = null;
		if(userIngredients.containsKey("ATTENDEES")){
			attendees = userIngredients.get("ATTENDEES").split(" ");
		}	
		
		Calendar calendar = gcalendarCreator.getCalendar(user.getUsername());
		
		Event event = buildEvent(userIngredients.get("SUMMARY"),
								 userIngredients.get("DESCRIPTION"),	
								 userIngredients.get("LOCATION"),
								 attendees,
								 Boolean.parseBoolean(userIngredients.get("ALL_DAY")),
								 new Date(userIngredients.get("START_DATE")),
								 new Date(userIngredients.get("END_DATE")),
								 userIngredients.get("TIME_ZONE"));
		
		event = calendar.events().insert("primary", event).execute();
		
		log.debug("Event created: " + event.getHtmlLink());
		log.debug("Performed action 'create-event' of channel Calendar.");		

	}
	
	private Event buildEvent(String summary, 
							 String description, 
							 String location, 
							 String[] attendees,
							 boolean allDay, 
							 Date start, 
							 Date end, 
							 String timeZone)
							 
	{
		Event event = new Event().setSummary(summary).setLocation(location).setDescription(description);
		
		if (allDay) {
			DateTime startDateTime = new DateTime(start);
			end = new Date(startDateTime.getValue() + 86400000);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    String startDateStr = dateFormat.format(start);
		    String endDateStr = dateFormat.format(end);
		    startDateTime = new DateTime(startDateStr);
		    DateTime endDateTime = new DateTime(endDateStr);
		    EventDateTime startEventDateTime = new EventDateTime().setDate(startDateTime);
		    EventDateTime endEventDateTime = new EventDateTime().setDate(endDateTime);
		    event.setStart(startEventDateTime);
		    event.setEnd(endEventDateTime);  
		} else {
			DateTime startDateTime = new DateTime(start);
			
			EventDateTime startEventDateTime = new EventDateTime()
					.setDateTime(startDateTime)
				    .setTimeZone(timeZone);
			event.setStart(startEventDateTime);
			
			DateTime endDateTime = new DateTime(end);
			EventDateTime endEventDateTime = new EventDateTime()
			    .setDateTime(endDateTime)
			    .setTimeZone(timeZone);
			event.setEnd(endEventDateTime);
		}

		if (attendees!=null) {
			List<EventAttendee> attendeesList = new ArrayList<>();
			for (String attendee : attendees) {
				System.out.println(attendee);				
				attendeesList.add(new EventAttendee().setEmail(attendee));
			}
			event.setAttendees(attendeesList);
		}
		
		return event;
	}

	

}
