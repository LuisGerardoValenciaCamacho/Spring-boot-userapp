package com.userapp.security;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	@Bean
	FilterRegistrationBean<CorsFilter> corsFilter() {
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.authorizeHttpRequests(authorizeRequest -> { authorizeRequest
			.requestMatchers(HttpMethod.GET, "/api/user/all").hasAnyRole("USER", "ADMIN")
			.requestMatchers(HttpMethod.GET, "/api/user/{id}").hasAnyRole("USER", "ADMIN")
			.requestMatchers(HttpMethod.POST, "/api/user/create").hasRole("ADMIN")
			.requestMatchers(HttpMethod.PUT, "/api/user/update/{id}").hasRole("ADMIN")
			.requestMatchers(HttpMethod.DELETE, "/api/user/delete/{id}").hasRole("ADMIN")
			.anyRequest().authenticated();
		})
		.addFilter(new JwtAuthenticationSecurity(authenticationConfiguration.getAuthenticationManager()))
		.addFilter(new JwtFilterValidationSecurity(authenticationConfiguration.getAuthenticationManager()))
		.csrf(config -> config.disable())
		.cors(cors -> cors.configurationSource(corsConfigurationSource()))
		.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.build();
//		return http.authorizeHttpRequests()
//			.requestMatchers(HttpMethod.GET, "/api/user/all").permitAll()
//			.anyRequest().authenticated()
//			.and()
//			.addFilter(new JwtAuthenticationSecurity(authenticationConfiguration.getAuthenticationManager()))
//			.addFilter(new JwtFilterValidationSecurity(authenticationConfiguration.getAuthenticationManager()))
//			.csrf(config -> config.disable())
//			.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//			.build();
	}
}
