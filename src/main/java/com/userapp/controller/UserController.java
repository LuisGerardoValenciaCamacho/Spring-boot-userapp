package com.userapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.userapp.model.DTO.UserDTO;
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
	public ResponseEntity<List<UserDTO>> getAll() {
		List<UserDTO> listUsers = new ArrayList<UserDTO>();
		listUsers = userService.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(listUsers);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id) {
		UserDTO user = userService.findById(id);
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<UserDTO> createUser (@RequestBody User user) {
		UserDTO newUser = userService.create(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User user) {
		UserDTO newUser = userService.update(id, user);
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		userService.delete(id);
		Map<String, Object> map = new HashMap<>();
		map.put("response", "User deleted");
		return ResponseEntity.accepted().body(map);
	}
}
