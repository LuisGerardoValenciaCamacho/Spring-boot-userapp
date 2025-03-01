package com.userapp.model;

import java.time.LocalDateTime;

public class ResponseError {

	private String message;
	private String error;
	private int code;
	private LocalDateTime date;
	
	public ResponseError() {
		super();
	}
	
	public ResponseError(String message, String error, int code, LocalDateTime date) {
		super();
		this.message = message;
		this.error = error;
		this.code = code;
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
	
}
