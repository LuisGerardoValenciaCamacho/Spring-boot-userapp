package com.userapp.service;

import java.util.List;

import com.userapp.model.User;

public interface IUserService {
	User findById(Long id);
	
	List<User> findAll();
	
	User create(User user);
	
	User update(Long id, User user);
	
	void delete(Long id);
}
