package it.ifttt.domain;

import java.util.List;

import org.bson.types.ObjectId;

public class RecipeClient {
	
	private ObjectId author_id; 
	private String title;
	private ObjectId trId;
	private ObjectId acId;
	private Boolean isActive;
	private List<Ingredient> ingrAc;
	private List<Ingredient> ingrTr;
	
	public ObjectId getAuthor_id() {
		return author_id;
	}
	public void setAuthor_id(ObjectId author_id) {
		this.author_id = author_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
		return "RecipeClient [author_id=" + author_id + ", title=" + title + ", trId=" + trId + ", acId="
				+ acId + ", isActive=" + isActive + ", ingrAc=" + ingrAc + ", ingrTr=" + ingrTr + "]";
	}
	

}