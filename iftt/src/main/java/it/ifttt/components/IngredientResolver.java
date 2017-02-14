package it.ifttt.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import it.ifttt.domain.Ingredient;

@Component
public class IngredientResolver {

	private final static Logger log = Logger.getLogger(IngredientResolver.class);

	public void resolve(List<Ingredient> userActionIngredient, List<Ingredient> triggerIngredient){
		List<String> tags;
		for(Ingredient ingr : userActionIngredient){
			tags = checkIfIngredientContainsTags(ingr);
			if(tags.size() > 0 ){
				for(String tag : tags){
					 String tagResolved = resolveTag(tag, triggerIngredient);
					 log.debug("Resolving tag "+tag+" with " +tagResolved);
					 ingr.setValue(replaceTagInTheIngredient(ingr, tag, tagResolved));
				}
			}
		}
	}
	
	private List<String> checkIfIngredientContainsTags(Ingredient ingredient){
		List<String> tags = new ArrayList<String>();
		for(String word : ingredient.getValue().split(" ")){
			if( word.charAt(0) == '@' ) //this word is a tag;
				tags.add(word);
		}
		return tags;
	}
	
	private String resolveTag(String tag, List<Ingredient> triggerIngredient){
		tag = tag.substring(1);
		for(Ingredient ingr : triggerIngredient){
			if(ingr.getName().equals(tag))
				return "@"+ingr.getValue();
		}
		return null;
	}
	
	private String replaceTagInTheIngredient(Ingredient ingredient, String tag, String tagResolved){
		return ingredient.getValue().replaceAll(tag, tagResolved.substring(1));
	}
}
