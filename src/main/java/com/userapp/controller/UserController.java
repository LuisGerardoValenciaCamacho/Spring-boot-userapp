package com.userapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userapp.model.User;
import com.userapp.service.IUserService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

	IUserService userService;
	
	public UserController(IUserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/all")
	public List<User> getAll() {
		List<User> listUsers = new ArrayList<User>();
		listUsers = userService.findAll();
		return listUsers;
	}
	
	@GetMapping("/{id}")
	public User getUserById(@PathVariable("id") Long id) {
		User user = userService.findById(id);
		return user;
	}
	
	@PostMapping("/create")
	public User createUser (@RequestBody User product) {
		User newUser = userService.create(product);
		return newUser;
	}
	
	@PutMapping("/update/{id}")
	public User updateUser(@PathVariable Long id, @RequestBody User product) {
		User newUser = userService.update(id, product);
		return newUser;
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteUser(@PathVariable Long id) {
		userService.delete(id);
	}
}
