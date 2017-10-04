package it.ifttt.channel.twitter.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.TweetData;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;

import it.ifttt.channel.ActionPerformer;
import it.ifttt.channel.gmail.action.SendEmail;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.User;
import it.ifttt.exceptions.UnauthorizedChannelException;
import it.ifttt.social_api_creators.TwitterTemplateCreator;

@Component
public class TweetStateAction implements ActionPerformer {

	/*
	 * Action: tweet-state
	 * 
	 * Ingredienti:
	 * - TEXT: testo del tweet
	 * - [REPLY_TO_STATUS_ID]: se presente, invia il tweet come risposta a quello con questo id 
	 * 
	*/
	
	public static final String TEXT_KEY = "text";
	public static final String REPLY_TO_STATUS_ID_KEY = "reply-to-status-id";
	
	@Autowired
	private TwitterTemplateCreator twitterTemplateCreator;
	
	private final static Logger log = Logger.getLogger(SendEmail.class);

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
	public void perform() throws UnauthorizedChannelException {
		
		log.debug("ACTION: i'm tweetStateAction...processing");
		log.debug("user: " + user.toString());
		log.debug(userIngredients.toString());
				
		// get twitter API for user
		Twitter twitter = twitterTemplateCreator.getTwitterTemplate(user.getUsername());
		
		// post new tweet
		TweetData tweet = new TweetData(userIngredients.get(TEXT_KEY));
		if (userIngredients.containsKey(REPLY_TO_STATUS_ID_KEY))
			tweet.inReplyToStatus(Long.parseLong(userIngredients.get(REPLY_TO_STATUS_ID_KEY)));
		twitter.timelineOperations().updateStatus(tweet);
		
		log.debug("ACTION: i'm tweetStateAction...done!");
	}

}
