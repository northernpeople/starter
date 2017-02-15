package com.stepan.service;

import java.util.List;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.stepan.model.Role;
import com.stepan.model.User;
import com.stepan.model.repo.RoleRepository;
import com.stepan.model.repo.UserRepository;

@Component
public class UserService implements UserDetailsService{

	@Autowired
	UserRepository repo;
	
	@Autowired
	RoleRepository roleRepo;

	public User byUserName(String username) {
		return repo.findByUsername(username);
	}

	public List<User> findAll() {
		return repo.findAll();
	}
	
	/**
	 * Creates new user and hashes password.
	 * @param user user with plain text password
	 * @return 
	 */
	public User create(User user, String inRole) {
		if( ! usernameTaken(user)){
			Role role = saveRole(new Role(inRole));
			user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
			user = saveAndFlush(user);
			user.linkNewRole(role);
			saveRole(role); // saves link
			return user;
		}	
		return null;
	}

	private boolean usernameTaken(User user) {
		return findAll().stream().map( u -> u.getUsername()).collect(Collectors.toSet()).contains(user.getUsername());
	}
	
	/**
	 * Updates user and rehashes password.
	 * @param user user with plain text password
	 */
	public void rehashPassword(User user){
		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
		repo.saveAndFlush(user);
	}

	public User byId(Long id) {
		return repo.findOne(id);
	}

	public void delete(Long id) {
		roleRepo.delete(byId(id).getRole().getId());
		repo.delete(id);	
	}
	
	public Role saveRole(Role role){
		return roleRepo.saveAndFlush(role);
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserDetails details = byUserName(userName);
		if (details == null) throw new UsernameNotFoundException("user not found: "+userName);
		return details;
	}

	public User saveAndFlush(User user) {
		return repo.saveAndFlush(user);
		
	}
	
	public User currentUser(){
		return byUserName(
				SecurityContextHolder.getContext().getAuthentication().getName()
				);
	}
}
