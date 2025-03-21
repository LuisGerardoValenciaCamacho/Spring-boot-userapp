package com.userapp.service;

import java.util.List;

import com.userapp.model.User;
import com.userapp.model.DTO.UserDTO;

public interface IUserService {
	UserDTO findById(Long id);
	
	User findByUsername(String username);
	
	List<UserDTO> findAll();
	
	UserDTO create(User user);
	
	UserDTO update(Long id, User user);
	
	void delete(Long id);
}
