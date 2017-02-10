package com.stepan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.stepan.model.User;

@Component
public class AdminSetup {
	
	@Value("${adminEmail}")
	String adminEmail;
	
	@Value("${adminPassword}")
	String adminPassword;
	
	@Autowired
	UserService userService;
	
	@Scheduled(fixedRate = Long.MAX_VALUE, initialDelay = 100)
	public void setUpAdmin(){
		userService.create(new User(adminEmail, adminPassword));
	}
}
