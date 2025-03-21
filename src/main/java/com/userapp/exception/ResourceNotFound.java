package com.userapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFound extends RuntimeException {
	
	public ResourceNotFound(Class<?> resourceClass, String message) {
		super(resourceClass.getSimpleName() + ": " + message);
	}
}
