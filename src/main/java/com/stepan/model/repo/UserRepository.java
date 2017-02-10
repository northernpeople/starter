package com.stepan.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stepan.model.User;


public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String Email);
}