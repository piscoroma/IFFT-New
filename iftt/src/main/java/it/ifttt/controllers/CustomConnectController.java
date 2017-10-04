package it.ifttt.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.view.RedirectView;

import it.ifttt.messages.ChannelStatusMessage;
import it.ifttt.services.ChannelService;
import it.ifttt.springSocialMongo.GoogleInterceptor;
import it.ifttt.springSocialMongo.TwitterInterceptor;

@CrossOrigin(origins = "*")
@Controller
public class CustomConnectController extends ConnectController{

	@Autowired
	ChannelService channelService;
	
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
	
	@RequestMapping(value="/status", method=RequestMethod.GET)
	public @ResponseBody List<ChannelStatusMessage> getAllStatus()
	{
		return channelService.getAllStatus();
	}
	
	@RequestMapping(value="/connect/{providerId}", method=RequestMethod.GET)
	public @ResponseBody ChannelStatusMessage getStatus(@PathVariable String providerId)
	{
		return channelService.getStatus(providerId);
	}
	
	
	

}
