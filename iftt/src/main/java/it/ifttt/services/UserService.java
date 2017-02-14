package it.ifttt.services;

import java.util.List;

import org.springframework.dao.DuplicateKeyException;

import it.ifttt.domain.User;
import it.ifttt.exceptions.DatabaseException;

public interface UserService {
	
	void addUser(User user) throws DatabaseException, DuplicateKeyException;
	User getUserByUsername(String username) throws DatabaseException, IllegalArgumentException;
	List<User> getAllUsers() throws DatabaseException;
	void deleteAllUsers() throws DatabaseException;
}
