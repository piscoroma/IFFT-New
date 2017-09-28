package it.ifttt.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import it.ifttt.domain.Channel;
import it.ifttt.domain.Trigger;

@Repository
public interface TriggerRepository extends MongoRepository<Trigger, Long> {
	List<Trigger> findByChannel(Channel channel);
	Trigger findById(ObjectId id);
	Trigger findByName(String name);

}
