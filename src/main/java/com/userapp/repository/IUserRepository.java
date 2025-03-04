package com.userapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.userapp.model.User;


@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUsername(String username);
}
