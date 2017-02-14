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

import it.ifttt.domain.RecipeInstance;
import it.ifttt.domain.RecipeStruct;
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
		/*List<RecipeInstance> recipeActiveInstanceList = new ArrayList<RecipeInstance>();
		try{
			recipeActiveInstanceList = recipeInstanceRepo.findByIsActive(true);
			for(RecipeInstance recipeInstance : recipeActiveInstanceList)
				recipeActiveInstanceMap.put(recipeInstance.getId(), recipeInstance);
		}catch(Exception e){
			log.debug("Initializing recipeService...Exception: " + e.getMessage());
			throw new RuntimeException(e);
		}*/
		log.debug("Initializing recipeService...done!");
	}
	
	@Override
	public void saveRecipeStruct(RecipeStruct recipeStruct) throws DatabaseException {
		try{
			recipeStructRepo.save(recipeStruct);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public List<RecipeStruct> getAllRecipesStruct(){
		return recipeStructRepo.findAll();
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
	public void saveRecipeInstance(RecipeInstance recipeInstance) throws DatabaseException {
		try{
			recipeInstanceRepo.save(recipeInstance);
		}catch(Exception e){
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public List<RecipeInstance> getAllRecipesInstance(){
		return recipeInstanceRepo.findAll();
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
		}catch(Exception e){
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
		}catch(Exception e){
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
		}catch(Exception e){
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
		}catch(Exception e){
			throw new DatabaseException(e);
		}
		if(recipeActiveInstanceMap.containsKey(id))
			recipeActiveInstanceMap.remove(id);
	}

}
