package it.ifttt.services;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.dao.DuplicateKeyException;

import it.ifttt.domain.User;
import it.ifttt.exceptions.DatabaseException;

public interface UserService {
	
	void addUser(User user) throws DatabaseException, DuplicateKeyException;
	User getUserById(ObjectId id) throws DatabaseException, IllegalArgumentException;
	User getUserByUsername(String username) throws DatabaseException, IllegalArgumentException;
	User getUserByEmail(String email) throws DatabaseException, IllegalArgumentException;
	boolean findUserByUsername(String username) throws DatabaseException;
	boolean findUserByEmail(String email) throws DatabaseException;
	List<User> getAllUsers() throws DatabaseException;
	void deleteAllUsers() throws DatabaseException;
}
