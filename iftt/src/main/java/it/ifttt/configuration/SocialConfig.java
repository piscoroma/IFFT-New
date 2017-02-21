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

import it.ifttt.springSocialMongo.ConnectionService;
import it.ifttt.springSocialMongo.MongoUsersConnectionRepository;

@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer {

	@Autowired
	ConnectionService mongoConnectionService;
		
	/*@Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        
        String googleClientId = "31535276894-dtfda6f9rt1f4kdscv433nkee3l3n94p.apps.googleusercontent.com";
        String googleClientSecret = "cYbyc8hzB1DuSYKZu6aVOcB2";
        
        registry.addConnectionFactory(new GoogleConnectionFactory(googleClientId, googleClientSecret
        		
		));
           
        return registry;
    }*/
	
	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
		cfConfig.addConnectionFactory(new GoogleConnectionFactory(
			env.getProperty("google.clientId"),
			env.getProperty("google.clientSecret")
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
	
	/*@Bean
	@Scope(value="prototype", proxyMode=ScopedProxyMode.INTERFACES)
    public ConnectionRepository connectionRepository() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
        }
        return getUsersConnectionRepository(connectionFactoryLocator()).createConnectionRepository(authentication.getName());
    }*/
	
}
