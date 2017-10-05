package it.ifttt.channel.weather.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.Forecast;
import com.github.fedy2.weather.data.unit.TimeConvention;

@Document(collection="weather")
public class Weather {
	
	@Id
	private ObjectId id;
	private String location;
	private String title; // rappresenta il canale meteo, è una stringa tipo "Yahoo! Weather - Troina, SC, IT"
	private String text; 
	private String todayCondition; // valore testuale che descrive le condizioni meteo previste per quel giorno
	private Integer temperature; // temperatura misurata (°C)
	private Integer humidity; // umidità misurata (in %)
	private Float pressure; // pressione misurata (in mb)
	private Float visibility; // visibilità misurata (km)
	private Float windSpeed; // velocità del vento misurata in (km/h)
	private Integer windDirection; // direzione del vento
	private int sunsetHours;
	private int sunsetMinutes;
	private int sunriseHours;
	private int sunriseMinutes;
	//@DBRef
	//private List<ForecastWeather> forecastList = new ArrayList<>(); ///previsioni (giorno corrente fino a un lapse di 9 giorni)
	Date downloaded; // specifica quando è stato scaricato il meteo

	public Weather()
	{}
	
	public Weather(Channel channel)
	{
		location=channel.getLocation().getCity();
		todayCondition = channel.getItem().getCondition().getText();		
		title = channel.getTitle();
		text = channel.getItem().getCondition().getText();
		temperature = channel.getItem().getCondition().getTemp();
		humidity = channel.getAtmosphere().getHumidity();
		pressure = channel.getAtmosphere().getPressure();
		visibility = channel.getAtmosphere().getVisibility();
		windSpeed = channel.getWind().getSpeed();
		windDirection = channel.getWind().getDirection();
		sunsetHours = channel.getAstronomy().getSunset().getHours();
		sunsetMinutes = channel.getAstronomy().getSunset().getMinutes();
		if(channel.getAstronomy().getSunset().getConvention()==TimeConvention.PM)
			sunsetHours+=12;
		sunriseHours = channel.getAstronomy().getSunrise().getHours();
		sunriseMinutes = channel.getAstronomy().getSunrise().getMinutes();
		if(channel.getAstronomy().getSunrise().getConvention()==TimeConvention.PM)
			sunriseHours+=12;
		downloaded=new Date();
		int lapse=0;
		/*for(Forecast forecast: channel.getItem().getForecasts())
		{
			ForecastWeather forceastWeather = new ForecastWeather(forecast,lapse++);
			forceastWeather.setRelatedWeather(this);
			forecastList.add(forceastWeather);
		}*/
	}
	
	public void fillWithNewData(Channel channel) {
		downloaded = new Date();
		location=channel.getLocation().getCity();
		todayCondition = channel.getItem().getCondition().getText();	
		title = channel.getTitle();
		text = channel.getItem().getForecasts().get(0).getText();
		temperature = channel.getItem().getCondition().getTemp();
		humidity = channel.getAtmosphere().getHumidity();
		pressure = channel.getAtmosphere().getPressure();
		visibility = channel.getAtmosphere().getVisibility();
		windSpeed = channel.getWind().getSpeed();
		windDirection = channel.getWind().getDirection();
		sunsetHours = channel.getAstronomy().getSunset().getHours();
		sunsetMinutes = channel.getAstronomy().getSunset().getMinutes();
		if(channel.getAstronomy().getSunset().getConvention()==TimeConvention.PM)
			sunsetHours+=12;
		sunriseHours = channel.getAstronomy().getSunrise().getHours();
		sunriseMinutes = channel.getAstronomy().getSunrise().getMinutes();
		if(channel.getAstronomy().getSunrise().getConvention()==TimeConvention.PM)
			sunriseHours+=12;
		int lapse=0;
		/*for(Forecast forecast: channel.getItem().getForecasts())
			forecastList.get(lapse++).fillWithNewData(forecast);*/

	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTodayCondition() {
		return todayCondition;
	}

	public void setCondition(String todayCondition) {
		this.todayCondition = todayCondition;
	}

	public Integer getTemperature() {
		return temperature;
	}

	public void setTemperature(Integer temperature) {
		this.temperature = temperature;
	}

	public Integer getHumidity() {
		return humidity;
	}

	public void setHumidity(Integer humidity) {
		this.humidity = humidity;
	}

	public Float getPressure() {
		return pressure;
	}

	public void setPressure(Float pressure) {
		this.pressure = pressure;
	}

	public Float getVisibility() {
		return visibility;
	}

	public void setVisibility(Float visibility) {
		this.visibility = visibility;
	}

	public Float getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(Float windSpeed) {
		this.windSpeed = windSpeed;
	}

	public Integer getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(Integer windDirection) {
		this.windDirection = windDirection;
	}

	/*public List<ForecastWeather> getForecastList() {
		return forecastList;
	}

	public void setForecastList(List<ForecastWeather> forecastList) {
		this.forecastList = forecastList;
	}*/

	public Date getDownloaded() {
		return downloaded;
	}

	public void setDownloaded(Date downloaded) {
		this.downloaded = downloaded;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setTodayCondition(String todayCondition) {
		this.todayCondition = todayCondition;
	}

	public int getSunsetHours() {
		return sunsetHours;
	}

	public void setSunsetHours(int sunsetHours) {
		this.sunsetHours = sunsetHours;
	}

	public int getSunsetMinutes() {
		return sunsetMinutes;
	}

	public void setSunsetMinutes(int sunsetMinutes) {
		this.sunsetMinutes = sunsetMinutes;
	}

	public int getSunriseHours() {
		return sunriseHours;
	}

	public void setSunriseHours(int sunriseHours) {
		this.sunriseHours = sunriseHours;
	}

	public int getSunriseMinutes() {
		return sunriseMinutes;
	}

	public void setSunriseMinutes(int sunriseMinutes) {
		this.sunriseMinutes = sunriseMinutes;
	}

}
