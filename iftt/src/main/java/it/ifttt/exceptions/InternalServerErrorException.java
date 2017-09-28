package it.ifttt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Internal Server Error")
public class InternalServerErrorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InternalServerErrorException() {
	}

	public InternalServerErrorException(String message) {
		super(message);
	}

	public InternalServerErrorException(Throwable cause) {
		super(cause);
	}

	public InternalServerErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public InternalServerErrorException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}