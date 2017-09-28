package it.ifttt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Trigger not found")
public class TriggerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TriggerNotFoundException() {
    }
	
	public TriggerNotFoundException(String message) {
		super(message);
	}

	public TriggerNotFoundException(Throwable cause) {
		super(cause);
	}

	public TriggerNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public TriggerNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}