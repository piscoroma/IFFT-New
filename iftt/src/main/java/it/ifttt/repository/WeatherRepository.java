package it.ifttt.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import it.ifttt.channel.weather.model.Weather;

@Repository
public interface WeatherRepository extends MongoRepository<Weather, Long> {
	Weather findByLocation(String location);
}
