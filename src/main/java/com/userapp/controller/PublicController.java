package com.userapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userapp.model.User;
import com.userapp.service.IUserService;

@RestController
@RequestMapping("/api/public")
@CrossOrigin("http://localhost:5173")
public class PublicController {
	
	private static final Logger logger = LoggerFactory.getLogger(PublicController.class);
	
	IUserService userService;
	
	public PublicController(IUserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/login")
	public boolean login(@RequestBody User user) {
		logger.info(user.getUsername());
		boolean result = userService.login(user.getUsername(), user.getPassword());
		return result;
	}

}
