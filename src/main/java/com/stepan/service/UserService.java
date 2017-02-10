package com.stepan.service;

import java.util.List;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stepan.model.User;
import com.stepan.model.repo.UserRepository;

@Component
public class UserService {

	@Autowired
	UserRepository repo;

	public User byEmail(String email) {
		return repo.findByEmail(email);
	}

	public List<User> findAll() {
		return repo.findAll();
	}
	
	/**
	 * Creates new user and hashes password.
	 * @param user user with plain text password
	 */
	public void create(User user) {
		if( ! findAll().stream().map( u -> u.getEmail()).collect(Collectors.toSet()).contains(user.getEmail())){
			user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
			repo.saveAndFlush(user);
		}
	}
	
	/**
	 * Updates user and rehashes password.
	 * @param user user with plain text password
	 */
	public void updatePassword(User user){
		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
		repo.saveAndFlush(user);
	}

	public User byId(Long id) {
		return repo.findOne(id);
	}

	public void delete(Long id) {
		repo.delete(id);
		
	}
}
