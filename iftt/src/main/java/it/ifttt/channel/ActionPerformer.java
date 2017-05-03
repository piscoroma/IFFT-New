package it.ifttt.channel;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import it.ifttt.domain.User;
import it.ifttt.domain.Ingredient;

public interface ActionPerformer {
	
	void setUser(User user);
	void setUserIngredients(List<Ingredient> userIngredients);
	//void setInjectedIngredients(List<Ingredient> injectableIngredients);
	void perform() throws Exception;
}
