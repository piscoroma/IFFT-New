package it.ifttt.domain;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "triggers")
public class Trigger {

	@Id
	private ObjectId id;
	@DBRef
	private Channel channel;
	private String name;
	private String description;
	private List<Ingredient> ingredients;
	private List<Ingredient> injectableIngredients;
	
	public Trigger(){
	}
	
	public Trigger(Channel channel, String name){
		this.channel = channel;
		this.description = name;
		//this.ingredients = new ArrayList<Ingredient>();
	}
	
	public Trigger(Channel channel, String name, List<Ingredient> ingredients, List<Ingredient> injectableIngredients){
		this.channel = channel;
		this.name = name;
		this.ingredients = ingredients;
		this.injectableIngredients = injectableIngredients;
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
	public List<Ingredient> getInjectableIngredients() {
		return injectableIngredients;
	}
	public void setInjectableIngredients(List<Ingredient> injectableIngredients) {
		this.injectableIngredients = injectableIngredients;
	}
	
}
