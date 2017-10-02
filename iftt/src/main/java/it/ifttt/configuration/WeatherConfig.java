package it.ifttt.configuration;

import javax.xml.bind.JAXBException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.fedy2.weather.YahooWeatherService;

@Configuration
public class WeatherConfig {
	
	@Bean
	public YahooWeatherService yahooWeatherService() throws JAXBException
	{
		return new YahooWeatherService();
	}
}