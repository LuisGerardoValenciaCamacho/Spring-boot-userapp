package com.userapp.controller;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.userapp.exception.ResourceNotFound;
import com.userapp.model.ResponseError;

@ControllerAdvice
public class ErrorController {
	
	private static final Logger logger = LoggerFactory.getLogger(ResponseError.class);

	@ExceptionHandler(exception = ResourceNotFound.class)
	public ResponseEntity<ResponseError> ResourceNotFound(ResourceNotFound ex) {
		ResponseError responseError = new ResponseError(ex.getLocalizedMessage(), "Not found", HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
		logger.error(ex.getLocalizedMessage());
		return new ResponseEntity<ResponseError>(responseError, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(exception = MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ResponseError> ResorceTypeMismatch(MethodArgumentTypeMismatchException ex) {
		logger.error(ex.getLocalizedMessage());
		ResponseError errorResponse = new ResponseError(ex.getLocalizedMessage(),  "Argument type mismatch", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
		return new ResponseEntity<ResponseError>(errorResponse, HttpStatus.BAD_REQUEST);
	}
}
