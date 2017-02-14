package it.ifttt.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import it.ifttt.domain.Channel;
import it.ifttt.domain.CollectionChannel;

@Repository
public interface ChannelRepository extends MongoRepository<Channel, Long>{
	Channel findById(ObjectId id);
	Channel findByName(String name);
	
}
