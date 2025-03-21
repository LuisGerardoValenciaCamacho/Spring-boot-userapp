package com.userapp.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthoritiyJsonCreator {
	
	@JsonCreator
	public SimpleGrantedAuthoritiyJsonCreator(@JsonProperty("authority") String role) {
		
	}
}
