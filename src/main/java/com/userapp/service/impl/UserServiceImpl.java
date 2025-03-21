package com.userapp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.userapp.exception.ResourceNotFound;
import com.userapp.model.Role;
import com.userapp.model.User;
import com.userapp.model.DTO.UserDTO;
import com.userapp.repository.IUserRepository;
import com.userapp.service.IUserService;
import com.userapp.util.BaseModelMapper;


@Service
public class UserServiceImpl extends BaseModelMapper<User, UserDTO> implements IUserService {
	
	private IUserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	public UserServiceImpl(IUserRepository iUserRepository, PasswordEncoder password, ModelMapper modelMapper) {
		super(modelMapper, User.class, UserDTO.class);
		this.userRepository = iUserRepository;
		this.passwordEncoder = password;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFound(User.class, "Usuario no encontrado"));
		UserDTO userDto = mapModelToDTO(user);
		return userDto;
	}
	
	@Override
	@Transactional(readOnly = true)
	public User findByUsername(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFound(User.class, "Usuario no encontrado"));
		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> findAll() {
		List<User> listUser = userRepository.findAll();
		List<UserDTO> listDto = listUser.stream().map(user -> mapModelToDTO(user)).toList();
		return listDto;
	}
	
	@Override
	@Transactional
	public UserDTO create(User user) {
		if(user.getUsername().isEmpty() || user.getUsername().isBlank()) {
			new ResourceNotFound(User.class, "Username not found");
		}
		if(user.getPassword().isEmpty() || user.getPassword().isBlank()) {
			new ResourceNotFound(User.class, "Password not found");
		}
		if(user.getEmail().isEmpty() || user.getEmail().isBlank()) {
			new ResourceNotFound(User.class, "Email not found");
		}
		List<Role> listRoles = new ArrayList<Role>();
		Role role = userRepository.findRoleByName("ROLE_USER").orElseThrow(() -> new ResourceNotFound(Role.class, "Role not found"));
		listRoles.add(role);
		User newUser = new User(user.getUsername(), passwordEncoder.encode(user.getPassword()), user.getEmail(), listRoles);
		newUser = userRepository.save(newUser);
		return mapModelToDTO(newUser);
	}

	@Override
	@Transactional
	public UserDTO update(Long id, User user) {
		User oldUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFound(User.class, "Usuario no encontrado"));
		oldUser.setUsername(user.getUsername());
		oldUser.setPassword(user.getPassword());
		oldUser.setEmail(oldUser.getEmail());
		oldUser = userRepository.save(oldUser);
		return mapModelToDTO(oldUser);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFound(User.class, "Usuario no encontrado"));
		userRepository.delete(user);
	}

}
