package it.ifttt.social_api_creators;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.google.api.Google;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;

import it.ifttt.exceptions.UnauthorizedChannelException;
import it.ifttt.springSocialMongo.ConnectionConverter;
import it.ifttt.springSocialMongo.MongoConnection;
import it.ifttt.springSocialMongo.MongoConnectionRepository;
import it.ifttt.springSocialMongo.MongoConnectionService;


@Component
@Scope("prototype")
public class GmailCreator {

	private final String GOOGLE_ID = "google";
	
	@Autowired
	private Environment environment;

	@Autowired
	private ApplicationContext appContext;
	
	@Autowired
	private MongoConnectionService mongoConnectionService;
	
	@Autowired
	private ConnectionRepository mongoConnectionRepository;
	
	@Autowired
	private ConnectionConverter connectionConverter;
	
	public Gmail getGmail(String username) throws GeneralSecurityException, IOException {
		
		MongoConnection userConnection = connectionConverter.convert(mongoConnectionService.getPrimaryConnection(
				username,
				GOOGLE_ID
		));
		if (userConnection == null)
			throw new UnauthorizedChannelException("No authorization found for user '" + username + "' on channel '" + GOOGLE_ID + "'.");
		
		checkAndRefreshConnection(userConnection);
		userConnection = connectionConverter.convert(mongoConnectionService.getPrimaryConnection(
				username,
				GOOGLE_ID
		));
		
		String accessToken = userConnection.getAccessToken();
		String refreshToken = userConnection.getRefreshToken();
		String clientId = environment.getProperty("google.clientId");
		String clientSecret = environment.getProperty("google.clientSecret");
		
		TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setAccessToken(accessToken);
		tokenResponse.setRefreshToken(refreshToken);
		
		Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
			.setTransport(GoogleNetHttpTransport.newTrustedTransport())
			.setJsonFactory(JacksonFactory.getDefaultInstance())
			.setTokenServerUrl(new GenericUrl("http://127.0.0.1:8080/IFTTT-New/"))
			.setClientAuthentication(new BasicAuthentication(clientId, clientSecret))
			.build()
			.setFromTokenResponse(tokenResponse);
		
		Gmail gmail = new Gmail.Builder(
				GoogleNetHttpTransport.newTrustedTransport(),
				JacksonFactory.getDefaultInstance(),
				credential
		)
            .setApplicationName("IFTTT Project")
            .build();
		
		return gmail;
	}
	
	private void checkAndRefreshConnection(MongoConnection userConnection) {
		
		// force authentication to this user to get it's google connection through spring social
		Authentication authentication = new UsernamePasswordAuthenticationToken((userConnection).getUserId(), "", null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		ConnectionRepository connectionRepository = appContext.getBean(ConnectionRepository.class);
		
		Connection<Google> connection = connectionRepository.getPrimaryConnection(Google.class);
		
		if (connection.hasExpired()) {
			
			// save the refresh token (it will became null after refreshing for some spring social mis-function, but it will be still good) 
			String refreshToken = userConnection.getRefreshToken();
			
			// refresh the connection
			connection.refresh();
			
			// store new access token
			connectionRepository.updateConnection(connection);
			
			// save the old still good refresh token
			userConnection = connectionConverter.convert(mongoConnectionService.getPrimaryConnection(userConnection.getUserId(), "GOOGLE_ID"));
			userConnection.setRefreshToken(refreshToken);
			mongoConnectionRepository.addConnection((Connection<?>)userConnection);
		}
	}
}
