package it.ifttt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Channel not found")
public class ChannelNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ChannelNotFoundException() {
	}

	public ChannelNotFoundException(String message) {
		super(message);
	}

	public ChannelNotFoundException(Throwable cause) {
		super(cause);
	}

	public ChannelNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ChannelNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}