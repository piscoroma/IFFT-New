package it.ifttt.channel.gcalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.ifttt.channel.ActionPerformer;
import it.ifttt.channel.GenericManager;
import it.ifttt.channel.TriggerEvent;
import it.ifttt.channel.gcalendar.trigger.CalendarEventCreated;
import it.ifttt.channel.gcalendar.trigger.CalendarEventStarted;
import it.ifttt.channel.gcalendar.action.CalendarCreateEvent;

@Component
public class GcalendarManager implements GenericManager {
	
	/*-----------------------TRIGGER-------------------------*/
	
	@Autowired
	private CalendarEventStarted calendarEventStarted;
	@Autowired
	private CalendarEventCreated calendarEventCreated;
	
	public TriggerEvent setTriggerEvent(String triggerName) {
		
		TriggerEvent event = null;
		
		switch(triggerName){
			case "CALENDAR_EVENT_STARTED":
				event = calendarEventStarted;
				break;
			case "CALENDAR_EVENT_CREATED":
				event = calendarEventCreated;
				break;
			default:
		}
		return event;
	}
		
	/*-----------------------ACTION-------------------------*/
	
	@Autowired
	private CalendarCreateEvent calendarCreateEvent;
	
	public ActionPerformer setActionPerformer(String actionName) {
		
		ActionPerformer action = null;
		
		switch(actionName){
			case "CALENDAR_CREATE_EVENT":
				action = calendarCreateEvent;
				break;
			default:
		}
		return action;
		
	}

}
