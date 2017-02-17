package com.stepan.web.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.stepan.model.Role;
import com.stepan.model.User;
import com.stepan.service.UserService;
import com.stepan.service.email.EmailService;


@RunWith(MockitoJUnitRunner.class)
public class AdminCTest {
	
	@InjectMocks
	AdminC controller;
	
	@Mock
	UserService uService;
	
	@Mock
	EmailService mService;

	MockMvc mockMvc;
	
	@Before
	public void setUpController(){
		mockMvc = MockMvcBuilders.standaloneSetup(controller).setViewResolvers(viewResolver()).build();
	}
	
	@Test
	public void resetPasswordShouldSendEmailAndRehash(){
		
	}
	
	@Test
	public void shouldShowMainPage() throws Exception{
		mockMvc.perform(get("/admin/main"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/main"))
			.andExpect( model().attributeExists("users", "user") );
	}
	
	@Test
	public void shouldResetPassword() throws Exception{
		User temp = new User("as@ss.ss", BCrypt.hashpw("password", BCrypt.gensalt()));
		Mockito.when(uService.byId(1L)).thenReturn(temp);
		
		mockMvc.perform(get("/admin/reset_pass_user/1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/main"));
		verify(uService).rehashPassword(temp);

		
	}
	
	@Test
	public void shouldDeleteUser() throws Exception{
		User u = new User("as@ss.ss", BCrypt.hashpw("password", BCrypt.gensalt()));
		u.setId(1L);
		Role r = new Role("ROLE_USER");
		r.setId(1L);
		u.linkNewRole(r);
		
		Mockito.when(uService.byId(1L)).thenReturn(u);
		mockMvc.perform(get("/admin/delete_user/1"))
			.andExpect(redirectedUrl("/admin/main"));
		verify(uService).delete(1L);
	}
	
	
	@Test
	public void shouldSaveUser() throws Exception{
		User u = new User("as@ss.ss", BCrypt.hashpw("password", BCrypt.gensalt()));
		u.setId(1L);
		Role r = new Role("ROLE_USER");
		r.setId(1L);
		u.linkNewRole(r);
		
		Mockito.when(uService.byId(1L)).thenReturn(u);
		mockMvc.perform(get("/admin/delete_user/1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/main"));

	}
	
	
	/* Not the best approach(not thymeleaf view resolver), but simple and tests that the views are resolved.*/
	private InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("");
		viewResolver.setSuffix(".html");
		return viewResolver;
	}


}
