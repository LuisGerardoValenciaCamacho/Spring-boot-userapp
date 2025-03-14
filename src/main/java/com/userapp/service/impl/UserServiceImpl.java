package com.userapp.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.userapp.exception.ResourceNotFound;
import com.userapp.model.User;
import com.userapp.repository.IUserRepository;
import com.userapp.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {
	
	private IUserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	
	public UserServiceImpl(IUserRepository iUserRepository, PasswordEncoder password) {
		this.userRepository = iUserRepository;
		this.passwordEncoder = password;
	}

	public User findById(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFound(User.class, "Usuario no encontrado"));
		return user;
	}
	
	public User findByUsername(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFound(User.class, "Usuario no encontrado"));
		return user;
	}

	public List<User> findAll() {
		List<User> listUser = userRepository.findAll();
		return listUser;
	}
	
	public boolean login(String username, String password) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFound(User.class, "Username not found"));
		if(user.getPassword().equals(password)) {
			return true;
		}
		return false;
	}

	public User create(User user) {
		if(user.getUsername().isEmpty() || user.getUsername().isBlank()) {
			new ResourceNotFound(User.class, "Username not found");
		}
		if(user.getPassword().isEmpty() || user.getPassword().isBlank()) {
			new ResourceNotFound(User.class, "Password not found");
		}
		if(user.getEmail().isEmpty() || user.getEmail().isBlank()) {
			new ResourceNotFound(User.class, "Email not found");
		}
		User newUser = new User(user.getUsername(), passwordEncoder.encode(user.getPassword()), user.getEmail());
		newUser = userRepository.save(newUser);
		return newUser;
	}

	public User update(Long id, User user) {
		User oldUser = findById(id);
		oldUser.setUsername(user.getUsername());
		oldUser.setPassword(user.getPassword());
		oldUser.setEmail(oldUser.getEmail());
		oldUser = userRepository.save(oldUser);
		return oldUser;
	}

	public void delete(Long id) {
		User user = findById(id);
		userRepository.delete(user);
	}

}
