package it.ifttt.springSocialMongo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;

public class TwitterInterceptor implements ConnectInterceptor<Twitter> {

	private static Logger log = LogManager.getLogger();
	
	@Override
	public void preConnect(ConnectionFactory<Twitter> provider, MultiValueMap<String, String> parameters, WebRequest request) {
		log.error("Twitter preConnect!");
	}
	
	@Override
	public void postConnect(Connection<Twitter> connection, WebRequest request) {
		connection.updateStatus("I've connected with the Spring Social Showcase!");
		log.error("Twitter postConnect!");	
	}

}