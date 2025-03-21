package com.userapp.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.userapp.constants.TokenJwtConstants;
import com.userapp.model.User;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationSecurity extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;

	public JwtAuthenticationSecurity(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		User user = null;
		UsernamePasswordAuthenticationToken authToken = null;
		try {
			user =  new ObjectMapper().readValue(request.getInputStream(), User.class);
			String username = user.getUsername();
			String password = user.getPassword();
			authToken = new UsernamePasswordAuthenticationToken(username, password);
		} catch (StreamReadException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (DatabindException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return authenticationManager.authenticate(authToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		String username = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername();
		Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
		boolean isAdmin = roles.stream().allMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
		String token = Jwts.builder()
			.claims()
				.add("authorities", new ObjectMapper().writeValueAsString(roles))
				.add("isAdmin", isAdmin)
				.add("username", username)
				.and()
			.signWith(TokenJwtConstants.KEY)
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + 600000))
			.compact();
		response.addHeader("Authorization", String.format(TokenJwtConstants.PREFIX_TOKEN + "%1$s", token));
		Map<String, Object> body = new HashMap<>();
		body.put("token", token);
		body.put("message", String.format("Hola %s, has iniciadio sesión con exito!", username));
		body.put("username", username);
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(200);
		response.setContentType("application/json");
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		Map<String, Object> body = new HashMap<>();
		body.put("message", "Error en la autenticación username o paswwrod incorrecto");
		body.put("error", failed.getMessage());
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(401);
		response.setContentType("application/json");
	}
	
}
