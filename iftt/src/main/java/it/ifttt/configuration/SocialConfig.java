package it.ifttt.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import it.ifttt.springSocialMongo.ConnectionService;
import it.ifttt.springSocialMongo.MongoUsersConnectionRepository;

@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer {

	@Autowired
	ConnectionService mongoConnectionService;
	
	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
		cfConfig.addConnectionFactory(new GoogleConnectionFactory(
			env.getProperty("google.clientId"),
			env.getProperty("google.clientSecret")
		));
		cfConfig.addConnectionFactory(new TwitterConnectionFactory(
			env.getProperty("twitter.appKey"),
			env.getProperty("twitter.appSecret")
		));
	}

	@Override
	public UserIdSource getUserIdSource() {
		return new AuthenticationNameUserIdSource();
	}

	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator cfLocator) {
		return new MongoUsersConnectionRepository(mongoConnectionService, cfLocator, Encryptors.noOpText());
	}
	
}
