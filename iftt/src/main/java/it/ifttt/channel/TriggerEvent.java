package it.ifttt.channel;

import java.util.Date;
import java.util.List;

import it.ifttt.domain.User;
import it.ifttt.domain.Ingredient;

public interface TriggerEvent {
	void setUser(User user);
	void setLastRefresh(Date lastRefresh);
	Date getLastRefresh();
	void setUserIngredients(List<Ingredient> ingredients);
	List<Object> raise() throws Exception;
	List<Ingredient> injectIngredients(List<Ingredient> injeactableIngredient, Object obj);
}
