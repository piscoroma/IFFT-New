package it.ifttt.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.ifttt.channel.ActionPerformer;
import it.ifttt.channel.GenericManager;
import it.ifttt.channel.TriggerEvent;
import it.ifttt.channel.gcalendar.GcalendarManager;
import it.ifttt.channel.gmail.GmailManager;
import it.ifttt.channel.twitter.TwitterManager;
import it.ifttt.channel.weather.WeatherManager;

@Component
public class ChannelManager {
	
	@Autowired
	private GcalendarManager gcalendarManager;
	@Autowired
	private GmailManager gmailManager;
	@Autowired
	private TwitterManager twitterManager;
	@Autowired
	private WeatherManager weatherManager;
	
	private GenericManager getChannelManager(String channelName){
		
		GenericManager genericManager = null;
		
		switch(channelName){
			case "GMAIL":
				genericManager = gmailManager;
				break;
			case "GCALENDAR":
				genericManager = gcalendarManager;
				break;
			case "TWITTER":
				genericManager = twitterManager;
				break;
			case "WHEATHER":
				genericManager = weatherManager;
				break;
			default:
		}
		return genericManager;
		
	}
	
	public TriggerEvent setTriggerEvent(String channelName, String triggerName){
		GenericManager genericManager = getChannelManager(channelName);
		return genericManager.setTriggerEvent(triggerName);
	}
	
	
	public ActionPerformer setActionPerformer(String channelName, String actionName){
		GenericManager genericManager = getChannelManager(channelName);
		return genericManager.setActionPerformer(actionName);
	}

}
	
	
	