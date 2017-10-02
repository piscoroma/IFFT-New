package it.ifttt.domain;

import java.util.List;

import org.bson.types.ObjectId;

public class RecipeClient {

	private String username; 
	private String title;
	private String description;
	private ObjectId trId;
	private ObjectId acId;
	private Boolean isActive;
	private List<Ingredient> ingrAc;
	private List<Ingredient> ingrTr;
	
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
	public ObjectId getTrId() {
		return trId;
	}
	public void setTrId(ObjectId trId) {
		this.trId = trId;
	}
	public ObjectId getAcId() {
		return acId;
	}
	public void setAcId(ObjectId acId) {
		this.acId = acId;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public List<Ingredient> getIngrAc() {
		return ingrAc;
	}
	public void setIngrAc(List<Ingredient> ingrAc) {
		this.ingrAc = ingrAc;
	}
	public List<Ingredient> getIngrTr() {
		return ingrTr;
	}
	public void setIngrTr(List<Ingredient> ingrTr) {
		this.ingrTr = ingrTr;
	}
	
	@Override
	public String toString() {
		return "RecipeClient [username=" + username + ", title=" + title + ", description=" + description +", trId=" + trId + ", acId="
				+ acId + ", isActive=" + isActive + ", ingrAc=" + ingrAc + ", ingrTr=" + ingrTr + "]";
	}
	

}