package it.ifttt.domain;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="recipesInstance")
public class RecipeInstance {
	
	@Id
	private ObjectId id;
	@DBRef
	private User user;
	private boolean isActive;
	private Date lastRefresh;
	@DBRef
	private RecipeStruct recipeStruct;
	private List<Ingredient> triggerIngredients;
	private List<Ingredient> actionIngredients;
	
	public RecipeInstance(){
	}
	
	public RecipeInstance(User user, boolean isActive, RecipeStruct recipeStruct, List<Ingredient> triggerIngredients, List<Ingredient> actionIngredients) {
		this.user = user;
		this.isActive = isActive;
		this.recipeStruct = recipeStruct;
		this.triggerIngredients = triggerIngredients;
		this.actionIngredients = actionIngredients;
	}
	
	public RecipeInstance(User user, boolean isActive, RecipeStruct recipeStruct) {
		this.user = user;
		this.isActive = isActive;
		this.recipeStruct = recipeStruct;
	}

	public ObjectId getId() {
		return id;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Date getLastRefresh() {
		return lastRefresh;
	}

	public void setLastRefresh(Date lastRefresh) {
		this.lastRefresh = lastRefresh;
	}

	public RecipeStruct getRecipeStruct() {
		return recipeStruct;
	}

	public void setRecipeStruct(RecipeStruct recipeStruct) {
		this.recipeStruct = recipeStruct;
	}

	public List<Ingredient> getTriggerIngredients() {
		return triggerIngredients;
	}

	public void setTriggerIngredients(List<Ingredient> triggerIngredients) {
		this.triggerIngredients = triggerIngredients;
	}

	public List<Ingredient> getActionIngredients() {
		return actionIngredients;
	}

	public void setActionIngredients(List<Ingredient> actionIngredients) {
		this.actionIngredients = actionIngredients;
	}
	
	
	
	
	
}
