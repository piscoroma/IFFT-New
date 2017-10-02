package it.ifttt.channel.weather.trigger;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.ifttt.channel.TriggerEvent;
import it.ifttt.channel.gmail.action.SendEmail;
import it.ifttt.channel.weather.model.Weather;
import it.ifttt.domain.Ingredient;
import it.ifttt.domain.User;
import it.ifttt.services.WeatherService;

@Component
public class CurrentWeatherTrigger implements TriggerEvent {

	/*
	 * Trigger: current-weather
	 * 
	 * Questo trigger va usato per monitorare le condizioni meteo in tempo reale, e non per le previsioni.
	 * Se si verifica una delle condizioni specificate come ingredienti, si scatena.
	 * 
	 * Ingredienti supportati:
	 * 
	 * (a parte location, che è obbligatorio, degli altri ne deve essere presente ALMENO UNO)
	 * 
	 * 1. condition: il trigger si scatena se le condizioni meteo attuali corrispondono al valore testuale passato 
	 * 			( Sunny | Showers | Rain | Mostly Sunny | Partly Cloudy | Thunderstorms | Scattered Showers | Scattered Thunderstorms )
	 * 
	 * 2. temperature-high: il trigger si scatena se la temperatura diventa maggiore di questa soglia (espressa in °C)
	 * 3. temperature-low: il trigger si scatena se la temperatura diventa minore di questa soglia (espressa in °C)
	 * 
	 * 4. humidity-high: il trigger si scatena se l'umidità diventa maggiore di questa soglia (l'umidità è espressa come percentuale)
	 * 5. humidity-low: il trigger si scatena se l'umidità diventa minore di questa soglia (l'umidità è espressa come percentuale)
	 * 
	 * 6. pressure-high: il trigger si scatena se la pressione diventa maggiore di questa soglia (espressa in mb millibar)
	 * 7. pressure-low: il trigger si scatena se la pressione diventa minore di questa soglia (espressa in mb)
	 * 
	 * 8. visibility-high: il trigger si scatena se la visibilità diventa maggiore di questa soglia (km)
	 * 9. visibility-low: il trigger si scatena se la visibilità diventa minore di questa soglia (km)
	 * 
	 * 10. wind-speed-high: il trigger si scatena se la velocità del vento diventa maggiore di questa soglia (km/h)
	 * 11. wind-speed-low: il trigger si scatena se la velocità del vento diventa minore di questa soglia (km/h)
	 * 
	 * 12. location: località di cui monitorare il meteo (OBBLIGATORIO)
	 *  
	 *  Se è presente più di un ingrediente di tipo diverso (ad esempio temperatura sotto i 5°C e direzione del vento = WNW)
	 *  allora il trigger viene scatenato solo se si verificano entrambi contemporaneamente.
	 *  
	 *  Vengono iniettati nell'action come ingredienti 'event:chaveoriginale' i valori misurati:
	 * 1. title: rappresenta il canale meteo, è una stringa tipo "Yahoo! Weather - Troina, SC, IT"
	 * 2. temperature: temperatura misurata (°C)
	 * 3. humidity: umidità misurata (in %)
	 * 4. pressure: pressione misurata (in mb)
	 * 5. visibility: visibilità misurata (km)
	 * 6. wind-speed: velocità del vento misurata (km/h)
	 * 7. condition: (vedi sopra)
	 * 8. location: (vedi sopra)
	 * 
	 */
	
	public static final String CONDITION_KEY = "condition";
	public static final String TEMPERATURE_HIGH_KEY = "temperature-high";
	public static final String TEMPERATURE_LOW_KEY = "temperature-low";
	public static final String HUMIDITY_HIGH_KEY = "humidity-high";
	public static final String HUMIDITY_LOW_KEY = "humidity-low";
	public static final String PRESSURE_HIGH_KEY = "pressure-high";
	public static final String PRESSURE_LOW_KEY = "pressure-low";
	public static final String VISIBILITY_HIGH_KEY = "visibility-high";
	public static final String VISIBILITY_LOW_KEY = "visibility-low";
	public static final String WIND_SPEED_HIGH_KEY = "wind-speed-high";
	public static final String WIND_SPEED_LOW_KEY = "wind-speed-low";
	public static final String LOCATION_KEY = "location";
	
	public static final String TITLE_KEY = "title";
	public static final String TEMPERATURE_KEY = "temperature";
	public static final String HUMIDITY_KEY = "humidity";
	public static final String PRESSURE_KEY = "pressure";
	public static final String VISIBILITY_KEY = "visibility";
	public static final String WIND_SPEED_KEY = "wind-speed";
	
	private final static Logger log = Logger.getLogger(SendEmail.class);
	
	private User user;
	private Date lastRefresh;
	private Map<String, String> userIngredients;
	
	@Autowired
	WeatherService weatherService;
	
	@Override
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public void setLastRefresh(Date lastRefresh) {
		this.lastRefresh = lastRefresh;
	}

	@Override
	public Date getLastRefresh() {
		return lastRefresh;
	}

	@Override
	public void setUserIngredients(List<Ingredient> ingredients) {
		userIngredients = new HashMap<String, String>();
		for(Ingredient ingr : ingredients)
			userIngredients.put(ingr.getName(), ingr.getValue());
	}

	@Override
	public List<Object> raise() throws Exception {
		
		List<Object> events = new ArrayList<Object>();
		Weather weather;
		
		// se il trigger è stato scatenato nell'ultimo giorno, non lo controllo più.
		if(lastRefresh!=null){
			Date now = new Date();
			long hours = ChronoUnit.HOURS.between(lastRefresh.toInstant(), now.toInstant());
			if(hours <= 24)
				return events;
		}
		log.debug("-->richiesta a weather");
		if ((weather = getNextWeather()) != null)
			events.add((Object)weather);
		
		//this.lastRefresh = new Date();
		return events;
	}

	@Override
	public List<Ingredient> injectIngredients(List<Ingredient> injeactableIngredient, Object obj) {
		
		List<Ingredient> injectedIngredients = new ArrayList<Ingredient>();
		
		Weather weather = (Weather) obj;
		
		for(Ingredient ingr : injeactableIngredient){
			switch(ingr.getName()){
			case CONDITION_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), weather.getText()));
				break;
			case LOCATION_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), weather.getLocation()));
				break;
			case TITLE_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), weather.getTitle()));
				break;
			case TEMPERATURE_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), String.valueOf(weather.getTemperature())));
				break;
			case HUMIDITY_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), String.valueOf(weather.getHumidity())));
				break;
			case PRESSURE_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), String.valueOf(weather.getPressure())));
				break;
			case VISIBILITY_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), String.valueOf(weather.getVisibility())));
				break;
			case WIND_SPEED_KEY: 
				injectedIngredients.add(new Ingredient(ingr.getName(), String.valueOf(weather.getWindSpeed())));
				break;
			default:
			}
		}
		return injectedIngredients;
	}
	
	/**
	 * Si connette alle API weather e recupera le condizioni meteo correnti 
	 * per la località specificata in questo trigger 
	 * @return condizioni meteo attuali
	 */
	private Weather getNextWeather() {
		
		Weather weather = weatherService.getWeatherByLocation(userIngredients.get(LOCATION_KEY));
		if (weatherSatisfyTrigger(weather)) {
			this.lastRefresh = new Date();
			return weather;
		}
		return null;
	}
	
	private boolean weatherSatisfyTrigger(Weather weather) {

		// [condition]
		if (userIngredients.containsKey(CONDITION_KEY) && !userIngredients.get(CONDITION_KEY).equals(weather.getText()))
			return false;
		// [temperature]
		if (userIngredients.containsKey(TEMPERATURE_HIGH_KEY) || userIngredients.containsKey(TEMPERATURE_LOW_KEY)) {
			if (!userIngredients.containsKey(TEMPERATURE_HIGH_KEY))
				//addIngredient(new Ingredient(TEMPERATURE_HIGH_KEY, String.valueOf(Integer.MAX_VALUE)));
			if (!userIngredients.containsKey(TEMPERATURE_LOW_KEY))
				//addIngredient(new Ingredient(TEMPERATURE_LOW_KEY, String.valueOf(Integer.MIN_VALUE)));
			if (weather.getTemperature() > Integer.parseInt(userIngredients.get(TEMPERATURE_LOW_KEY)) 
					&& weather.getTemperature() < Integer.parseInt(userIngredients.get(TEMPERATURE_HIGH_KEY)))
				return false;
		}
		// [humidity]
		if (userIngredients.containsKey(HUMIDITY_HIGH_KEY) || userIngredients.containsKey(HUMIDITY_LOW_KEY)) {
			if (!userIngredients.containsKey(HUMIDITY_HIGH_KEY))
				//addIngredient(new Ingredient(HUMIDITY_HIGH_KEY, "100"));
			if (!userIngredients.containsKey(HUMIDITY_LOW_KEY))
				//addIngredient(new Ingredient(HUMIDITY_LOW_KEY, "0"));
			if (weather.getHumidity() > Integer.parseInt(userIngredients.get(HUMIDITY_LOW_KEY)) 
					&& weather.getHumidity() < Integer.parseInt(userIngredients.get(HUMIDITY_HIGH_KEY)))
				return false;
		}
		// [pressure]
		if (userIngredients.containsKey(PRESSURE_HIGH_KEY) || userIngredients.containsKey(PRESSURE_LOW_KEY)) {
			if (!userIngredients.containsKey(PRESSURE_HIGH_KEY))
				//addIngredient(new Ingredient(PRESSURE_HIGH_KEY, String.valueOf(Float.MAX_VALUE)));
			if (!userIngredients.containsKey(PRESSURE_LOW_KEY))
				//addIngredient(new Ingredient(PRESSURE_LOW_KEY, String.valueOf(Float.MIN_VALUE)));
			if (weather.getPressure() > Float.parseFloat(userIngredients.get(PRESSURE_LOW_KEY)) 
					&& weather.getPressure() < Float.parseFloat(userIngredients.get(PRESSURE_HIGH_KEY)))
				return false;
		}
		// [visibility]
		if (userIngredients.containsKey(VISIBILITY_HIGH_KEY) || userIngredients.containsKey(VISIBILITY_LOW_KEY)) {
			if (!userIngredients.containsKey(VISIBILITY_HIGH_KEY))
				//addIngredient(new Ingredient(VISIBILITY_HIGH_KEY, String.valueOf(Float.MAX_VALUE)));
			if (!userIngredients.containsKey(VISIBILITY_LOW_KEY))
				//addIngredient(new Ingredient(VISIBILITY_LOW_KEY, String.valueOf(Float.MIN_VALUE)));
			if (weather.getVisibility() > Float.parseFloat(userIngredients.get(VISIBILITY_LOW_KEY)) 
					&& weather.getVisibility() < Float.parseFloat(userIngredients.get(VISIBILITY_HIGH_KEY)))
				return false;
		}
		// [wind speed]
		if (userIngredients.containsKey(WIND_SPEED_HIGH_KEY) || userIngredients.containsKey(WIND_SPEED_LOW_KEY)) {
			if (!userIngredients.containsKey(WIND_SPEED_HIGH_KEY))
				//addIngredient(new Ingredient(WIND_SPEED_HIGH_KEY, String.valueOf(Float.MAX_VALUE)));
			if (!userIngredients.containsKey(WIND_SPEED_LOW_KEY))
				//addIngredient(new Ingredient(WIND_SPEED_LOW_KEY, String.valueOf(Float.MIN_VALUE)));
			if (weather.getWindSpeed() > Float.parseFloat(userIngredients.get(WIND_SPEED_LOW_KEY)) 
					&& weather.getWindSpeed() < Float.parseFloat(userIngredients.get(WIND_SPEED_HIGH_KEY)))
				return false;
		}
		return true;
	}

}
