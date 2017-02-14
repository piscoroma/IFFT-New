package it.ifttt.domain;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "actions")
public class Action {

	@Id
	private ObjectId id;
	@DBRef
	private Channel channel;
	private String name;
	private String description;
	private List<Ingredient> ingredients;
	
	public Action(){
	}
	
	public Action(Channel channel, String name){
		this.channel = channel;
		this.name = name;
		//this.ingredients = new ArrayList<Ingredient>();
	}
	
	public Action(Channel channel, String name, List<Ingredient> ingredients){
		this.channel = channel;
		this.name = name;
		this.ingredients = ingredients;
	}
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

}
