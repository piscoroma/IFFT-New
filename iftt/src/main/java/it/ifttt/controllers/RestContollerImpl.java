package it.ifttt.controllers;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestContollerImpl implements it.ifttt.controllers.RestController {

	private final static Logger log = Logger.getLogger(RestContollerImpl.class);
	
	@Override
	@RequestMapping(value="/helloWorld", method=RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public String HelloWorld() {
		log.debug("This is a debug message");
        log.info("This is an info message");
        log.warn("This is a warn message");
        log.error("This is an error message");
		return "Hello World";
	}
	
	
	@RequestMapping(value="/helloWorldAuth", method=RequestMethod.GET)
	@ResponseStatus(value=HttpStatus.OK)
	public String HelloWorldAuth() {
		log.debug("Auth: This is a debug message");
        log.info("Auth: This is an info message");
        log.warn("Auth: This is a warn message");
        log.error("Auth: This is an error message");
		return "Auth: Hello World";
	}
	
}
