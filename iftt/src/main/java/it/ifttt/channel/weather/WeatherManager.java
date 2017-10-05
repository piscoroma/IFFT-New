package it.ifttt.channel.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.ifttt.channel.ActionPerformer;
import it.ifttt.channel.GenericManager;
import it.ifttt.channel.TriggerEvent;
import it.ifttt.channel.weather.trigger.CurrentWeatherTrigger;

@Component
public class WeatherManager implements GenericManager {

	/*-----------------------TRIGGER-------------------------*/
	
	@Autowired
	private CurrentWeatherTrigger currentWeatherTrigger;
	
	@Override
	public TriggerEvent setTriggerEvent(String triggerName) {
		
		TriggerEvent event = null;
		
		switch(triggerName){
			case "CURRENT_WEATHER_TRIGGER":
				event = currentWeatherTrigger;
				break;
			default:
		}
		return event;

	}
	
	/*-----------------------ACTION-------------------------*/

	@Override
	public ActionPerformer setActionPerformer(String actionName) {
		// TODO Auto-generated method stub
		return null;
	}

}
