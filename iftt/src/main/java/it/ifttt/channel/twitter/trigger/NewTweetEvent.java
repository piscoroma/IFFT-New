package it.ifttt.channel.twitter.trigger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;

import it.ifttt.channel.TriggerEvent;
import it.ifttt.channel.gmail.action.SendEmail;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.User;
import it.ifttt.social_api_creators.TwitterTemplateCreator;

@Component
public class NewTweetEvent implements TriggerEvent {

	/*
	 * Trigger: new-tweet
	 * 
	 * Ingredienti supportati:
	 * - FROM: il trigger si scatena solo se chi ha postato il tweet coincide con questo
	 * - TEXT: il trigger si scatena solo se il testo del tweet CONTIENE questo testo
	 * 
	 * L'evento che si genera se il trigger è verificato contiene i seguenti elementi:
	 * - FROM: chi ha postato il tweet
	 * - TEXT: testo del tweet 
	 * - TWEET_ID: id del tweet
	 * - NLIKES: numero di likes ricevuti nel tweet
	 * - DATE: data di post del tweet  
	 * 
	 */
	
	private final String FROM_KEY = "from";
	private final String TEXT_KEY = "text";
	private final String TWEET_ID_KEY = "tweet-id";
	private final String NLIKES_KEY = "favourite-count";
	private final String DATE_KEY = "date";

	@Autowired
	private TwitterTemplateCreator twitterTemplateCreator;
	
	private final static Logger log = Logger.getLogger(SendEmail.class);
	
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
	public List<Object> raise() throws Exception {
		
		List<Object> events = new ArrayList<Object>();
		
		if(lastRefresh!=null){
			Date now = new Date();
			long seconds = (now.getTime()-lastRefresh.getTime())/1000;
			if(seconds <= 60)
				//return
				throw new Exception("too much request, seconds: " + seconds);
		}
		log.debug("-->richiesta a twitter");
		List<Tweet> newTweets = null;
		try{
			newTweets = getNewTweets();
		}catch(Exception e){
			if(this.lastRefresh==null)
				this.lastRefresh = new Date();
			throw e;
		}
		for(Tweet tweet : newTweets){
			if(tweetSatisfyTrigger(tweet))
				events.add((Object)tweet);
		}
	
		this.lastRefresh = new Date();
		return events;
	}
	
	private List<Tweet> getNewTweets() throws Exception{
		
		// get twitter API for user
		Twitter twitter = twitterTemplateCreator.getTwitterTemplate(user.getUsername());

		List<Tweet> tweets = null;
		try{
			tweets = twitter.timelineOperations().getHomeTimeline();
		}catch(Exception e){
			throw e;
		}
		Collections.reverse(tweets);
		
		List<Tweet> newTweets = new ArrayList<Tweet>();
		for (Tweet tweet : tweets) {
			if (tweet.getCreatedAt().after(lastRefresh))
				newTweets.add(tweet);				
		}
		return newTweets;
	}
	
	private boolean tweetSatisfyTrigger(Tweet tweet) {
		
		if (userIngredients.containsKey(FROM_KEY) && !userIngredients.get(FROM_KEY).equals(tweet.getFromUser()))
				return false;
		if (userIngredients.containsKey(TEXT_KEY) && !tweet.getText().contains(userIngredients.get(TEXT_KEY)))
			return false;
		return true;
	}

	@Override
	public List<Ingredient> injectIngredients(List<Ingredient> injeactableIngredient, Object obj) {
		
		List<Ingredient> injectedIngredients = new ArrayList<Ingredient>();
		
		Tweet event = (Tweet)obj;
		
		for(Ingredient ingr : injeactableIngredient){
			switch(ingr.getName()){
			case FROM_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), event.getFromUser()));
				break;
			case TEXT_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), event.getText()));
				break;
			case TWEET_ID_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), Long.toString(event.getId())));
				break;
			case NLIKES_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), event.getFavoriteCount().toString()));
				break;
			case DATE_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), event.getCreatedAt().toString()));
				break;
			default:
			}
		}
		
		return injectedIngredients;
		
	}

}
