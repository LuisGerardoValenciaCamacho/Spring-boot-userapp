package com.userapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ConfigurationSecurity {
	
	private AuthenticationConfiguration authenticationConfiguration;
	
	public ConfigurationSecurity(AuthenticationConfiguration config) {
		this.authenticationConfiguration = config;
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager() throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.authorizeHttpRequests()
			.requestMatchers(HttpMethod.GET, "/api/user/all").permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilter(new JwtAuthenticationSecurity(authenticationConfiguration.getAuthenticationManager()))
			.addFilter(new JwtFilterValidationSecurity(authenticationConfiguration.getAuthenticationManager()))
			.csrf(config -> config.disable())
			.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.build();
	}
}
