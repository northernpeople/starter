package com.stepan.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.stepan.model.User;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class UserServiceTest {
	
	@Autowired
	UserService service;
	
	String email = "aa@ss.ss";
	String password = "asdfasdf";
	String role = "ROLE_USER";

	
	@Before
	public void setUp(){
		service.create(new User(email, password), role);
	}
	
	@Test
	public void shouldFetchByUserName() {
		assertNotNull(service.byUserName(email));
	}
	
	@Test
	public void shouldFindAll(){
		assertTrue(service.findAll().size() > 0);
	}
	
	@Test
	public void shouldCreateInRole(){
		assertTrue(service.byUserName(email)
				.getRole()
				.getName()
				.equalsIgnoreCase(role));
		assertNotNull(service.byUserName(email).getRole().getId());
	}
	
	@Test
	public void shouldHashPasswordOnCreation(){
		service.create( new User(email, password ), role);
		assertTrue(service.byUserName(email).getPassword().length() > password.length());
				
	}
	
	@Test 
	public void shouldTestTakenName(){
		assertTrue(service.usernameTaken(new User(email, password )));
	}
	
	@Test
	public void shouldRehashPassword(){
		User  user = service.byUserName(email);
		String old = user.getPassword();
		service.rehashPassword(user);
		user = service.byUserName(email);
		assertNotEquals(old, user.getPassword());

	}
	
	@Test
	public void shouldFetchById(){	
		assertNotNull(
				service.byId(
						service.byUserName(email).getId()
						));
	}
	
	@Test
	public void shouldDeleteUserAndRole(){
		service.delete(1L);
		assertEquals(service.allRoles().size(), 0);
		assertEquals(service.findAll().size(), 0);
		
	}
	
	@Test
	public void shouldLoadUserDetailsByUsername(){
		assertNotNull(service.loadUserByUsername(email));
		assertNotNull(service.loadUserByUsername(email).getAuthorities());

	}
	
	@Test(expected=UsernameNotFoundException.class)
	public void shouldNotLoadUserDetailsByWrongUsername(){
		service.loadUserByUsername("wrong@email.com");
	}
	
	
}
