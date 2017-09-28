package it.ifttt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED)
public class UnauthorizedChannelException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnauthorizedChannelException() {
    }
	
	public UnauthorizedChannelException(String message) {
		super(message);
	}

	public UnauthorizedChannelException(Throwable cause) {
		super(cause);
	}

	public UnauthorizedChannelException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnauthorizedChannelException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}

