package it.ifttt.controllers;

import javax.ws.rs.core.MediaType;

import it.ifttt.domain.User;
import it.ifttt.exceptions.ConflictException;
import it.ifttt.exceptions.DatabaseException;
import it.ifttt.exceptions.InternalServerErrorException;
import it.ifttt.services.UserService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.DuplicateKeyException;

import it.ifttt.configuration.MongoObjectIdTypeAdapter;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControllerImpl implements UserController {

	private final static Logger log = Logger.getLogger(RestContollerImpl.class);
	Gson gson = new GsonBuilder().registerTypeAdapter(ObjectId.class, new MongoObjectIdTypeAdapter()).create();;

	@Autowired
	UserService userService;
	
	@Override
	@CrossOrigin
	@RequestMapping(value="/register", method=RequestMethod.POST, produces = {MediaType.APPLICATION_JSON})
	@ResponseStatus(value=HttpStatus.CREATED)
	public void register(@RequestBody String json_user) {
		System.out.println("AAA" + json_user.toString());
		User user = gson.fromJson(json_user, User.class);
		System.out.println("BBB" + user.toString());
		boolean ready_to_register = false;
		try{
			if(userService.findUserByUsername(user.getUsername()) == false){
				if(userService.findUserByEmail(user.getEmail()) == false){
					user.setRole("user");
					ready_to_register = true;
				}
			}
			if(ready_to_register){
				userService.addUser(user);
				System.out.println("New user registered: " + user.getUsername());
			}
			else
				throw new ConflictException();
		}catch(DuplicateKeyException e){
			throw new ConflictException();
		}catch(DatabaseException e){
			throw new InternalServerErrorException();
		}
	}

}
