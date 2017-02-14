package it.ifttt.services;

import java.util.List;

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
	public User getUserByUsername(String username) throws DatabaseException, IllegalArgumentException {
		User user = null;
		try{
			user =  userRepo.findByUsername(username);
			if(user == null)
				throw new IllegalArgumentException("Username not found");
			else 
				return user;
		}catch(Exception e){
			throw new DatabaseException(e);
		}	
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
