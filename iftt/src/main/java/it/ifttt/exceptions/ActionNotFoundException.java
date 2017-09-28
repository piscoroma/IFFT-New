package it.ifttt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Action not found")
public class ActionNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ActionNotFoundException() {
    }
	
	public ActionNotFoundException(String message) {
		super(message);
	}

	public ActionNotFoundException(Throwable cause) {
		super(cause);
	}

	public ActionNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActionNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}