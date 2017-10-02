package it.ifttt.springSocialMongo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.google.api.Google;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;

public class GoogleInterceptor implements ConnectInterceptor<Google> {

	private static Logger log = LogManager.getLogger();

	@Override
	public void preConnect(ConnectionFactory<Google> provider, MultiValueMap<String, String> parameters, WebRequest request) {
		log.error("Google preConnect!");
	}
	
	@Override
	public void postConnect(Connection<Google> connection, WebRequest request) {
		log.error("Google postConnect!");
	}

}
