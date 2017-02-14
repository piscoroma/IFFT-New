package it.ifttt.channel.gcalendar.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import it.ifttt.channel.ActionPerformer;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.User;

@Component
public class CalendarCreateEvent implements ActionPerformer {

	private final static Logger log = Logger.getLogger(CalendarCreateEvent.class);

	private User user;
	private List<Ingredient> userIngredients;
	
	@Override
	public void setUser(User user) {
		this.user = user;	
	}

	@Override
	public void setUserIngredients(List<Ingredient> userIngredients) {
		this.userIngredients = userIngredients;
	}

	@Override
	public void perform() {
		log.debug("ACTION: i'm CalendarCreateEvent");
		log.debug("user: " + user.toString());
		log.debug(userIngredients.toString());
	}

	

}
