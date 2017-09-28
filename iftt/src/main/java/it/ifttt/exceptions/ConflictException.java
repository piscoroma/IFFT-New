package it.ifttt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="Conflict Exception")
public class ConflictException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConflictException() {
	}

	public ConflictException(String message) {
		super(message);
	}

	public ConflictException(Throwable cause) {
		super(cause);
	}

	public ConflictException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConflictException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
