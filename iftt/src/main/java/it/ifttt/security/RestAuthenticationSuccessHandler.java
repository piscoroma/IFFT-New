package it.ifttt.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.ifttt.configuration.MongoObjectIdTypeAdapter;
import it.ifttt.domain.User;
import it.ifttt.repository.UserRepository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    @Autowired
    private UserRepository UserRepository;
    
    //Gson gson = new GsonBuilder().registerTypeAdapter(ObjectId.class, new MongoObjectIdTypeAdapter()).create();;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {
    	User user = UserRepository.findByUsername(authentication.getName());
        //response.setContentType("application/json");
    	SecurityUtils.sendResponse(response, HttpServletResponse.SC_OK, user);
    }
}
