package it.ifttt.channel.gmail.trigger;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import it.ifttt.channel.TriggerEvent;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.User;

@Component
public class MailReceivedEvent implements TriggerEvent {

	/*
	 * Trigger: mail-received
	 * 
	 * Ingredienti supportati:
	 * - FROM: il trigger si scatena solo se il mittente dell'email coincide con questo
	 * - SUBJECT: il trigger si scatena solo se l'oggetto dell'email CONTIENE questo testo
	 * - BODY: il trigger si scatena solo se il testo dell'email CONTIENE questo testo
	 * - DATE: il trigger si scatena solo la data di ricezione dell'email coincide con questa
	 * 
	 * L'evento che si genera se il trigger è verificato contiene i seguenti elementi:
	 * - FROM: mittente dell'email
	 * - CC: eventuali altri destinatari
	 * - SUBJECT: oggetto dell'email
	 * - BODY: testo dell'email
	 * - DATE: data di ricezione dell'email
	 * 
	 * Questi e tutti gli altri parametri vengono iniettati come ingredienti nell'action.
	 */
    
	@Override
	public void setUser(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLastRefresh(Date lastRefresh) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Date getLastRefresh() {
		return null;
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
