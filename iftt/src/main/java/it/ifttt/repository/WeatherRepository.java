package it.ifttt.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import it.ifttt.channel.weather.model.Weather;

@Repository
public interface WeatherRepository extends MongoRepository<Weather, Long> {
	Weather save(Weather weather);
	List<Weather> findAll();
	Weather findByLocation(String location);
}
