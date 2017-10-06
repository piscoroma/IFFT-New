package it.ifttt.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.ifttt.domain.Action;
import it.ifttt.domain.RecipeInstance;
import it.ifttt.domain.RecipeStruct;
import it.ifttt.domain.User;
import it.ifttt.exceptions.DatabaseException;
import it.ifttt.repository.RecipeInstanceRepository;
import it.ifttt.repository.RecipeStructRepository;

@Service
public class RecipeServiceImpl implements RecipeService {

	private final static Logger log = Logger.getLogger(RecipeServiceImpl.class);

	@Autowired
	private RecipeStructRepository recipeStructRepo;
	@Autowired
	private RecipeInstanceRepository recipeInstanceRepo;

	private Map<ObjectId, RecipeInstance> recipeActiveInstanceMap;
		
	@PostConstruct
	private void init() throws RuntimeException{
		log.debug("Initializing recipeService...");
		recipeActiveInstanceMap = new HashMap<ObjectId, RecipeInstance>();
		List<RecipeInstance> recipeActiveInstanceList = new ArrayList<RecipeInstance>();
		try{
			recipeActiveInstanceList = recipeInstanceRepo.findByIsActive(true);
			for(RecipeInstance recipeInstance : recipeActiveInstanceList)
				recipeActiveInstanceMap.put(recipeInstance.getId(), recipeInstance);
		}catch(Exception e){
			log.debug("Initializing recipeService...Exception: " + e.getMessage());
			throw new RuntimeException(e);
		}
		log.debug("Initializing recipeService...done!");
	}
	
	@Override
	public RecipeStruct saveRecipeStruct(RecipeStruct recipeStruct) throws DatabaseException {
		try{
			return recipeStructRepo.save(recipeStruct);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public List<RecipeStruct> getAllRecipesStruct() throws DatabaseException{
		try{
			return recipeStructRepo.findAll();
		}catch(Exception e){
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public List<RecipeStruct> getAllPublicRecipesStruct() {
		try{
			boolean isPublic = true;
			return recipeStructRepo.findByIsPublic(isPublic);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public RecipeStruct getRecipeStruct(ObjectId id_trigger, ObjectId id_action) throws DatabaseException{
		List<RecipeStruct> recipeStructList;
		try{
			recipeStructList = getAllRecipesStruct();
		}catch(Exception e){
			throw new DatabaseException(e);
		}
		for(RecipeStruct recipeStruct : recipeStructList){
			if(recipeStruct.getTrigger().getId() == id_trigger)
				if(recipeStruct.getAction().getId() == id_action)
					return recipeStruct;
		}
		return null;
	}
	
	@Override
	public void updateRecipeStruct(RecipeStruct recipeStruct) throws DatabaseException, IllegalArgumentException {
		try{
			if(recipeStructExists(recipeStruct.getId()))
				recipeStructRepo.save(recipeStruct);
			else
				throw new IllegalArgumentException("RecipeStruct not found");
		}catch(DatabaseException e){
			throw new DatabaseException(e);
		}	
	}
	
	private boolean recipeStructExists(ObjectId id) throws DatabaseException{
		RecipeStruct recipeStruct = null;
		try{
			recipeStruct = recipeStructRepo.findById(id);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
		if(recipeStruct == null)
			return false;
		return true;
	}
	
	@Override
	public void deleteAllRecipesStruct() throws DatabaseException {
		try{
			recipeStructRepo.deleteAll();
		}catch(Exception e){
			throw new DatabaseException(e);
		}
	}	
	
	@Override
	public RecipeInstance saveRecipeInstance(RecipeInstance recipeInstance) throws DatabaseException {
		try{
			return recipeInstanceRepo.save(recipeInstance);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public void updateRecipeInstance(RecipeInstance recipeInstance) throws DatabaseException, IllegalArgumentException {
		try{
			if(recipeInstanceExists(recipeInstance.getId()))
				recipeInstanceRepo.save(recipeInstance);
			else
				throw new IllegalArgumentException("RecipeInstance not found");
		}catch(DatabaseException e){
			throw new DatabaseException(e);
		}	
	}
	
	private boolean recipeInstanceExists(ObjectId id) throws DatabaseException{
		RecipeInstance recipeInstance = null;
		try{
			recipeInstance = recipeInstanceRepo.findById(id);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
		if(recipeInstance == null)
			return false;
		return true;
	}
	
	@Override
	public List<RecipeInstance> getAllRecipesInstance() throws DatabaseException{
		try{
			return recipeInstanceRepo.findAll();
		}catch(Exception e){
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public List<RecipeInstance> getAllRecipesInstanceByUser(User user) throws DatabaseException {
		try{
			return recipeInstanceRepo.findByUser(user);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public List<RecipeInstance> getAllActiveRecipesInstance() {
		return new ArrayList<RecipeInstance>(recipeActiveInstanceMap.values());
	}
	
	@Override
	public void activeRecipeInstance(ObjectId id) throws DatabaseException, IllegalArgumentException{
		RecipeInstance recipeInstance = null;
		try{
			recipeInstance = recipeInstanceRepo.findById(id);
			if(recipeInstance == null)
				throw new IllegalArgumentException("RecipeInstance not found");
			recipeInstance.setActive(true);
			recipeInstanceRepo.save(recipeInstance);
		}catch(DatabaseException e){
			throw new DatabaseException(e);
		}
		recipeActiveInstanceMap.put(id, recipeInstance);		
	}

	@Override
	public void disableRecipeInstance(ObjectId id) throws DatabaseException, IllegalArgumentException {
		RecipeInstance recipeInstance = null;
		try{
			recipeInstance = recipeInstanceRepo.findById(id);
			if(recipeInstance == null)
				throw new IllegalArgumentException("RecipeInstance not found");
			recipeInstance.setActive(false);
			recipeInstanceRepo.save(recipeInstance);
		}catch(DatabaseException e){
			throw new DatabaseException(e);
		}
		recipeActiveInstanceMap.remove(id);
	}
	
	@Override
	public void updateRefreshTime(ObjectId id) throws DatabaseException {
		RecipeInstance recipeInstance = null;
		try{
			recipeInstance = recipeInstanceRepo.findById(id);
			if(recipeInstance == null)
				throw new IllegalArgumentException("RecipeInstance not found");
			recipeInstance.setLastRefresh(new Date());
			recipeInstanceRepo.save(recipeInstance);
		}catch(DatabaseException e){
			throw new DatabaseException(e);
		}
		recipeActiveInstanceMap.put(id, recipeInstance);
	}
	
	@Override
	public void updateRefreshTime(ObjectId id, Date lastRefresh) throws DatabaseException, IllegalArgumentException {
		RecipeInstance recipeInstance = null;
		try{
			recipeInstance = recipeInstanceRepo.findById(id);
			if(recipeInstance == null)
				throw new IllegalArgumentException("RecipeInstance not found");
			recipeInstance.setLastRefresh(lastRefresh);
			recipeInstanceRepo.save(recipeInstance);
		}catch(DatabaseException e){
			throw new DatabaseException(e);
		}
		recipeActiveInstanceMap.put(id, recipeInstance);
		
	}

	@Override
	public void deleteAllRecipesInstance() throws DatabaseException {
		try{
			recipeInstanceRepo.deleteAll();
		}catch(Exception e){
			throw new DatabaseException(e);
		}
		recipeActiveInstanceMap.clear();
	}

	@Override
	public void deleteRecipeInstance(ObjectId id) throws DatabaseException, IllegalArgumentException {
		RecipeInstance recipeInstance = null;
		try{
			recipeInstance = recipeInstanceRepo.findById(id);
			if(recipeInstance == null)
				throw new IllegalArgumentException("RecipeInstance not found");
			recipeInstanceRepo.delete(recipeInstance);
		}catch(DatabaseException e){
			throw new DatabaseException(e);
		}
		if(recipeActiveInstanceMap.containsKey(id))
			recipeActiveInstanceMap.remove(id);
	}

}
