package it.ifttt.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.ifttt.channel.ActionPerformer;
import it.ifttt.channel.GenericManager;
import it.ifttt.channel.TriggerEvent;
import it.ifttt.channel.gcalendar.GcalendarManager;
import it.ifttt.channel.gmail.GmailManager;

@Component
public class ChannelManager {
	
	@Autowired
	private GcalendarManager gcalendarManager;
	@Autowired
	private GmailManager gmailManager;
	/*@Autowired
	private WheaterManager wheatherManager;
	@Autowired
	private TwitterManager twitterManager;*/
	
	private GenericManager getChannelManager(String channelName){
		
		GenericManager genericManager = null;
		
		switch(channelName){
			case "GMAIL":
				genericManager = gmailManager;
				break;
			case "GCALENDAR":
				genericManager = gcalendarManager;
				break;
			/*case "WHEATHER":
				genericManager = wheatherManager;
				break;
			case "TWITTER":
				genericManager = twitterManager;
				break;*/
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
	
	
	
	
	/*public enum ChannelName{
		GMAIL,
		WHEATHER,
		GCALENDAR,
		TWITTER
	}
	
	private String getChannelServiceName(Channel channel){
		ChannelName currentChannel = ChannelName.valueOf(channel.getName());
		switch(currentChannel){
		case GMAIL:
			return "GmailServiceImpl";
		case WHEATHER:
			return "WheatherServiceImpl";
		case GCALENDAR:
			return "GCalendarServiceImpl";
		case TWITTER:
			return "TwitterServiceImpl";
		}
		return null;
	}
	
	private GenericChannel getChannelClass(Channel channel) throws Exception{
		try{
			String channelServiceName = getChannelServiceName(channel);
			Class<?>channelClass = Class.forName("it.ifttt.channels."+channelServiceName);
			GenericChannel channelService = (GenericChannel)channelClass.cast(channelClass.newInstance());
			return channelService;
		}catch(Exception e){
			throw e;
		}
	}
	
	
	
	public boolean checkIfTriggerIsVerified(Trigger trigger, List<Ingredient> ingredients) throws Exception{
		GenericChannel channelService = null;
		try{
			channelService = getChannelClass(trigger.getChannel());
		}catch(Exception e){
			throw e;
		}
		try{
			return channelService.checkIfTriggerIsVerified(trigger, ingredients);
		}catch(Exception e){
			throw e;
		}
	}
	
	public void performAction(Action action, List<Ingredient> ingredients) throws Exception{
		GenericChannel channelService = null;
		try{
			channelService = getChannelClass(action.getChannel());
		}catch(Exception e){
			throw e;
		}
		try{
			channelService.performAction(action, ingredients);
		}catch(Exception e){
			throw e;
		}
	}*/
	
	
}
