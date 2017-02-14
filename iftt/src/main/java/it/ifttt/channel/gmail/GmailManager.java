package it.ifttt.channel.gmail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.ifttt.channel.ActionPerformer;
import it.ifttt.channel.GenericManager;
import it.ifttt.channel.TriggerEvent;
import it.ifttt.channel.gmail.action.SendEmail;
import it.ifttt.channel.gmail.trigger.MailReceivedEvent;

@Component
public class GmailManager implements GenericManager {

/*-----------------------TRIGGER-------------------------*/
	
	@Autowired
	private MailReceivedEvent mailReceivedEvent;
	
	public TriggerEvent setTriggerEvent(String triggerName) {
		
		TriggerEvent event = null;
		
		switch(triggerName){
			case "MAIL_RECEIVED_EVENT":
				event = mailReceivedEvent;
				break;
			default:
		}
		return event;
	}
		
	/*-----------------------ACTION-------------------------*/
	
	@Autowired
	private SendEmail sendEmail;
	
	public ActionPerformer setActionPerformer(String actionName) {
		
		ActionPerformer action = null;
		
		switch(actionName){
			case "SEND_EMAIL":
				action = sendEmail;
				break;
			default:
		}
		return action;
		
	}

}
