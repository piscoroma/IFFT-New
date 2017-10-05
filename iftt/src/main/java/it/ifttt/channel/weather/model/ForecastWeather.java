package it.ifttt.channel.weather.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.github.fedy2.weather.data.Forecast;

public class ForecastWeather {

	@Id
	private ObjectId id;
	//@DBRef
	private Weather relatedWeather;
	private int lapse; // 0-9
	private String text;
	private int temperatureHigh;
	private int temperatureLow;
	
	public ForecastWeather() {
	}

	public ForecastWeather(Forecast forecast, int lapse)
	{
		this.lapse=lapse;
		text = forecast.getText();
		temperatureHigh = forecast.getHigh();
		temperatureLow = forecast.getLow();
	}

	public ObjectId getId() {
		return id;
	}

	public Weather getRelatedWeather() {
		return relatedWeather;
	}

	public void setRelatedWeather(Weather relatedWeather) {
		this.relatedWeather = relatedWeather;
	}

	public int getLapse() {
		return lapse;
	}

	public void setLapse(int lapse) {
		this.lapse = lapse;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getTemperatureHigh() {
		return temperatureHigh;
	}

	public void setTemperatureHigh(int temperatureHigh) {
		this.temperatureHigh = temperatureHigh;
	}

	public int getTemperatureLow() {
		return temperatureLow;
	}

	public void setTemperatureLow(int temperatureLow) {
		this.temperatureLow = temperatureLow;
	}

	public void fillWithNewData(Forecast forecast) {
		text = forecast.getText();
		temperatureHigh = forecast.getHigh();
		temperatureLow = forecast.getLow();
	}
	
}
