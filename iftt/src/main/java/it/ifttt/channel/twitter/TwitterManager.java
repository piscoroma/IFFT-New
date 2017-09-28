package it.ifttt.channel.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.ifttt.channel.ActionPerformer;
import it.ifttt.channel.GenericManager;
import it.ifttt.channel.TriggerEvent;
import it.ifttt.channel.twitter.action.TweetStateAction;
import it.ifttt.channel.twitter.trigger.NewTweetEvent;

@Component
public class TwitterManager implements GenericManager {

/*-----------------------TRIGGER-------------------------*/
	
	@Autowired
	private NewTweetEvent newTweetEvent;
	
	public TriggerEvent setTriggerEvent(String triggerName) {
		
		TriggerEvent event = null;
		
		switch(triggerName){
			case "NEW_TWEET_EVENT":
				event = newTweetEvent;
				break;
			default:
		}
		return event;
	}
		
	/*-----------------------ACTION-------------------------*/
	
	@Autowired
	private TweetStateAction tweetStateAction;
	
	public ActionPerformer setActionPerformer(String actionName) {
		
		ActionPerformer action = null;
		
		switch(actionName){
			case "TWEET_STATE_ACTION":
				action = tweetStateAction;
				break;
			default:
		}
		return action;
		
	}

}
