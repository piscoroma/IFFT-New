package it.ifttt.services;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DuplicateKeyException;

import it.ifttt.domain.User;
import it.ifttt.exceptions.DatabaseException;
import it.ifttt.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepo;
	
	
	@Override
	public void addUser(User user) throws DatabaseException, DuplicateKeyException {
		try{
			userRepo.save(user);
		}catch(DuplicateKeyException dke){
			throw dke;
		}catch(Exception e){
			throw new DatabaseException(e);
		}
	}

	@Override
	public User getUserById(ObjectId id) throws DatabaseException, IllegalArgumentException{
		User user = null;
		try{
			user = userRepo.findById(id);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
		if(user == null)
			throw new IllegalArgumentException("User not found");
		return user;
	}
	
	@Override
	public User getUserByUsername(String username) throws DatabaseException, IllegalArgumentException {
		User user = null;
		try{
			user =  userRepo.findByUsername(username);
		}catch(Exception e){
			throw new DatabaseException(e);
		}	
		if(user == null)
			throw new IllegalArgumentException("Username not found");
		return user;
	}
	
	@Override
	public User getUserByEmail(String email) throws DatabaseException, IllegalArgumentException {
		User user = null;
		try{
			user =  userRepo.findByEmail(email);
		}catch(Exception e){
			throw new DatabaseException(e);
		}	
		if(user == null)
			throw new IllegalArgumentException("Username not found");
		return user;
	}
	
	@Override
	public boolean findUserByUsername(String username) throws DatabaseException{
		User user = null;
		try{
			user =  userRepo.findByUsername(username);
		}catch(Exception e){
			throw new DatabaseException(e);
		}	
		if(user == null)
			return false;
		return true;
	}
	
	@Override
	public boolean findUserByEmail(String email) throws DatabaseException{
		User user = null;
		try{
			user =  userRepo.findByEmail(email);
		}catch(Exception e){
			throw new DatabaseException(e);
		}	
		if(user == null)
			return false;
		return true;
	}

	@Override
	public List<User> getAllUsers() throws DatabaseException {
		try{
			return userRepo.findAll();
		}catch(Exception e){
			throw new DatabaseException(e);
		}
	}

	@Override
	public void deleteAllUsers() throws DatabaseException {
		try{
			userRepo.deleteAll();
		}catch(Exception e){
			throw new DatabaseException(e);
		}		
	}

}
