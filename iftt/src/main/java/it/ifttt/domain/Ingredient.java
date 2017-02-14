package it.ifttt.domain;

public class Ingredient {

	private String name;
	private String value;
	
	public Ingredient(){
	}
	
	public Ingredient(String name){
		this.name = name;
	}
	
	public Ingredient(String name, String value){
		this.name = name;
		this.value = value;
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
	
	@Override
	public String toString() {
		return "Ingredient [name=" + name + ", value=" + value + "]";
	}
}
