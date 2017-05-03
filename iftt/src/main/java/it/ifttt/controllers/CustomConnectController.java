package it.ifttt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.view.RedirectView;

import it.ifttt.springSocialMongo.GoogleInterceptor;
import it.ifttt.springSocialMongo.TwitterInterceptor;

@Controller
public class CustomConnectController extends ConnectController{

	@Autowired
	public CustomConnectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
		super(connectionFactoryLocator, connectionRepository);
		this.addInterceptor(new GoogleInterceptor());
		this.addInterceptor(new TwitterInterceptor());
	}
	
	@Override
	protected RedirectView connectionStatusRedirect(String providerId, NativeWebRequest request) {
		//HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
		String path = "/#/loginWithSocialSuccessfull";
		return new RedirectView(path, true);
	}
	

}
