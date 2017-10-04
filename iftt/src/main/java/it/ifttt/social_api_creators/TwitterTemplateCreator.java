package it.ifttt.social_api_creators;

import javax.ws.rs.NotAuthorizedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.social.RateLimitExceededException;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Component;

import it.ifttt.exceptions.UnauthorizedChannelException;
import it.ifttt.springSocialMongo.ConnectionConverter;
import it.ifttt.springSocialMongo.MongoConnection;
import it.ifttt.springSocialMongo.MongoConnectionRepository;
import it.ifttt.springSocialMongo.MongoConnectionService;

@Component
public class TwitterTemplateCreator {
	
	public static final String TWITTER_ID = "twitter";
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private MongoConnectionService mongoConnectionService;
	
	@Autowired
	private ConnectionConverter connectionConverter;
 
	public Twitter getTwitterTemplate(String username) throws UnauthorizedChannelException {
	   
		MongoConnection userConnection = connectionConverter.convert(mongoConnectionService.getPrimaryConnection(
				username,
				TWITTER_ID
		));
		if (userConnection == null)
			throw new UnauthorizedChannelException("No authorization found for user '" + username + "' on channel '" + TWITTER_ID + "'.");
	   
		String consumerKey = environment.getProperty("twitter.appKey");
		String consumerSecret = environment.getProperty("twitter.appSecret");
		String accessToken = userConnection.getAccessToken();
		String accessTokenSecret = userConnection.getSecret();
	   
		TwitterTemplate twitterTemplate = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
		return twitterTemplate;
	   
   }
	
public void assertAuthorizedChannel(String username) throws UnauthorizedChannelException{
		
		Twitter twitter = null;
		
		twitter = getTwitterTemplate(username);
		if (!twitter.isAuthorized()) {
			System.out.println("Twitter NON è un canale autorizzato");
			MongoConnection userConnection = connectionConverter.convert(mongoConnectionService.getPrimaryConnection(
					username,
					TWITTER_ID
			));
			if (userConnection != null)
				//userConnectionRepository.delete(userConnection);
			throw new UnauthorizedChannelException(TWITTER_ID);
		}
		try {
			twitter.timelineOperations().getHomeTimeline();
		} catch (NotAuthorizedException | RateLimitExceededException ex) {
			System.out.println("Twitter NON è un canale autorizzato");
			MongoConnection userConnection = connectionConverter.convert(mongoConnectionService.getPrimaryConnection(
					username,
					TWITTER_ID
			));
			if (userConnection != null)
				//userConnectionRepository.delete(userConnection);
			throw new UnauthorizedChannelException(TWITTER_ID);
		}
		System.out.println("Twitter è un canale autorizzato");
	}
}
