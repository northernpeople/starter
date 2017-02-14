package com.stepan.service;

import java.util.List;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
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

	public User byUserName(String userName) {
		return repo.findByUserName(userName);
	}

	public List<User> findAll() {
		return repo.findAll();
	}
	
	/**
	 * Creates new user and hashes password.
	 * @param user user with plain text password
	 * @return 
	 */
	public User create(User user) {
		if( ! findAll().stream().map( u -> u.getUserName()).collect(Collectors.toSet()).contains(user.getUserName())){
			user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
			return repo.saveAndFlush(user);
		}
		return null;
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
	
	public Role saveRole(Role role){
		return roleRepo.saveAndFlush(role);
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		System.out.println("finding by username "+ userName);
		UserDetails details = byUserName(userName);
		if (details == null) throw new UsernameNotFoundException("user not found: "+userName);
		return details;
	}

	public User saveAndFlush(User user) {
		return repo.saveAndFlush(user);
		
	}
}
