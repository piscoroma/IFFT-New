package it.ifttt.services;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.YahooWeatherService.LimitDeclaration;
import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.unit.DegreeUnit;

import it.ifttt.channel.weather.model.Weather;
import it.ifttt.repository.WeatherRepository;

@Service
public class WeatherService {
	
	@Autowired
    private WeatherRepository weatherRepository;
    @Autowired
    private YahooWeatherService yahooService;
    
    public Weather save(Weather weather) {
    	return weatherRepository.save(weather);
    }

	public String downloadWeatherData(String location) {
		// esempio: la location digitata dall'utente può essere 'roma'
		Weather weather = weatherRepository.findByLocation(location);
		if(weather==null)
		{
			// la location corretta (in newWeather) sarà 'Rome'
			Weather newWeather = new Weather(downloadData(location));
			// devo accertarmi che la location giusta ('Rome') non esista già!
			Weather possibleExistingWeather = weatherRepository.findByLocation(newWeather.getLocation());
			if(possibleExistingWeather==null)
			{
				weather = newWeather;
				weatherRepository.save(weather);
			}
			else
				weather = possibleExistingWeather;
		}
		return weather.getLocation();
	}

	private Channel downloadData(String location) {
		try {
			// get info for the specified location
			LimitDeclaration limit = yahooService.getForecastForLocation(location, DegreeUnit.CELSIUS);

			return limit.all().get(0);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Weather getWeatherByLocation(String location) {
		Weather weather = weatherRepository.findByLocation(location);
		if(weather == null){
			String rightLocation = downloadWeatherData(location);
			weather = weatherRepository.findByLocation(rightLocation);
		}
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		Calendar deadline = Calendar.getInstance();
		deadline.setTime(weather.getDownloaded());
		deadline.add(Calendar.MINUTE, 10);
		if(now.after(deadline))
		{
			System.out.println("Weather with location: "+weather.getLocation()+"needs to be refreshed");
			weather.fillWithNewData(downloadData(weather.getLocation()));
			weatherRepository.save(weather);
		}
		return weather;
	}
	
}
