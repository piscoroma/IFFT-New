package it.ifttt.channel.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.ifttt.channel.ActionPerformer;
import it.ifttt.channel.GenericManager;
import it.ifttt.channel.TriggerEvent;
import it.ifttt.channel.weather.trigger.CurrentWeatherTrigger;
import it.ifttt.channel.weather.trigger.ReportWeatherTrigger;
import it.ifttt.channel.weather.trigger.SunWeatherTrigger;

@Component
public class WeatherManager implements GenericManager {

	/*-----------------------TRIGGER-------------------------*/
	
	@Autowired
	private CurrentWeatherTrigger currentWeatherTrigger;
	/*@Autowired
	private ReportWeatherTrigger reportWeatherTrigger;
	@Autowired
	private SunWeatherTrigger sunWeatherTrigger;*/
	
	@Override
	public TriggerEvent setTriggerEvent(String triggerName) {
		
		TriggerEvent event = null;
		
		switch(triggerName){
			case "CURRENT_WEATHER_TRIGGER":
				event = currentWeatherTrigger;
				break;
			/*case "REPORT_WEATHER_TRIGGER":
				event = reportWeatherTrigger;
				break;
			case "SUN_WEATHER_TRIGGER":
				event = sunWeatherTrigger;
				break;*/
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
