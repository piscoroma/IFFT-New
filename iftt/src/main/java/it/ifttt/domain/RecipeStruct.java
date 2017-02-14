package it.ifttt.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="recipesStruct")
public class RecipeStruct {
	
	@Id
	private ObjectId id;
	@DBRef
	private User author;
	private String description;
	private boolean isPublic;
	@DBRef
	private Trigger trigger;
	@DBRef
	private Action action;
	
	public RecipeStruct(){
	}
	
	public RecipeStruct(User author, String description, boolean isPublic, Trigger trigger, Action action) {
		this.author = author;
		this.description = description;
		this.isPublic = isPublic;
		this.trigger = trigger;
		this.action = action;
	}
	
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isPublic() {
		return isPublic;
	}
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	public Trigger getTrigger() {
		return trigger;
	}
	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	
	

}
