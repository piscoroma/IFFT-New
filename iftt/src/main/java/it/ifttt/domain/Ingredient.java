package it.ifttt.domain;

import java.util.ArrayList;
import java.util.List;

public class Ingredient {

	private String name;
	private String value;
	private String description;
	private String type;
	private boolean isMandatory;
	private List<String> possibleValues;
	
	public Ingredient(){
	}
	
	public Ingredient(String name){
		this.name = name;
	}
	
	public Ingredient(String name, String value){
		this.name = name;
		this.value = value;
	}
	
	public Ingredient(String name, boolean isMandatory, String description, String type, List<String> possibleValues ){
		this.name = name;
		this.isMandatory = isMandatory;
		this.description = description;
		this.type = type;
		if(possibleValues==null)
			this.possibleValues = new ArrayList<String>();
		else
			this.possibleValues = possibleValues;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isMandatory() {
		return isMandatory;
	}
	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}
	public List<String> getPossibleValues() {
		return possibleValues;
	}
	public void setPossibleValues(List<String> possibleValues) {
		this.possibleValues = possibleValues;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
