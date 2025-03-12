package com.userapp.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userapp.constants.TokenJwtConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilterValidationSecurity extends BasicAuthenticationFilter {

	public JwtFilterValidationSecurity(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String header = request.getHeader(TokenJwtConstants.HEADER_AUTHORIZATION);
		if(header == null || !header.startsWith(TokenJwtConstants.PREFIX_TOKEN)) {
			chain.doFilter(request, response);
			return;
		}
		String token = header.replace(TokenJwtConstants.PREFIX_TOKEN, "");
		try {			
			Claims claims = Jwts.parser().verifyWith((SecretKey) TokenJwtConstants.KEY).build().parseSignedClaims(token).getPayload();
			String username = claims.getSubject();
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(request, response);
		} catch(JwtException e) {
			logger.error(e.getMessage());
			Map<String, String> body = new HashMap<>();
			body.put("error", e.getMessage());
			body.put("message", "Token is not valid");
			response.getWriter().write(new ObjectMapper().writeValueAsString(body));
			response.setStatus(403);
			response.setContentType("application/json");
		}
	}
}