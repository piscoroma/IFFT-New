package it.ifttt.domain;

import java.util.List;
import org.bson.types.ObjectId;

public class RecipeClient {

	private String username; 
	private String title;
	private String description;
	private Boolean isPublic;
	private Boolean isActive;
	//private ObjectId trId;
	private String trigger_name;
	private List<Ingredient> trigger_ingredients;
	//private ObjectId acId;
	private String action_name;
	private List<Ingredient> action_ingredients;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Boolean getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}
	public String getTrigger_name() {
		return trigger_name;
	}
	public void setTrigger_name(String trigger_name) {
		this.trigger_name = trigger_name;
	}
	public List<Ingredient> getTrigger_ingredients() {
		return trigger_ingredients;
	}
	public void setTrigger_ingredients(List<Ingredient> trigger_ingredients) {
		this.trigger_ingredients = trigger_ingredients;
	}
	public String getAction_name() {
		return action_name;
	}
	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}
	public List<Ingredient> getAction_ingredients() {
		return action_ingredients;
	}
	public void setAction_ingredients(List<Ingredient> action_ingredients) {
		this.action_ingredients = action_ingredients;
	}
	
	@Override
	public String toString() {
		return "RecipeClient [username=" + username + ", title=" + title + ", isPublic=" + isPublic + ", isActive="
				+ isActive + ", trigger_name=" + trigger_name + ", trigger_ingredients=" + trigger_ingredients
				+ ", action_name=" + action_name + ", action_ingredients=" + action_ingredients + "]";
	}
	

}