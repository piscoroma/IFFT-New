package it.ifttt.configuration;

import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;

@Configuration
@EnableMongoRepositories("it.ifttt.repository")
public class MongoConfig {
	
	@Bean
	public MongoClient mongoClient() throws UnknownHostException{
		return new MongoClient("localhost", 27017);
	}
	
	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception{
		return new SimpleMongoDbFactory(mongoClient(), "ifttt");
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception{
		return new MongoTemplate(mongoDbFactory());
	}

}