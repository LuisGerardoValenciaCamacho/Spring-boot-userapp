package com.userapp.constants;

import java.security.Key;
import io.jsonwebtoken.Jwts;

public class TokenJwtConstants {

	public final static Key KEY = Jwts.SIG.HS512.key().build();
	public final static String PREFIX_TOKEN = "Bearer ";
	public final static String HEADER_AUTHORIZATION = "Authorization";
}
