package com.userapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.userapp.model.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

}
