package it.ifttt.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import it.ifttt.domain.User;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {
	User findById(ObjectId id);
	User findByUsername(String username);
	User findByEmail(String email);
}
