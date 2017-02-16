package com.stepan.web.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import com.stepan.model.User;
import com.stepan.service.UserService;
import com.stepan.service.email.EmailService;


@RunWith(MockitoJUnitRunner.class)
public class UserCTest {
	
	MockMvc mockMvc;
	
	@InjectMocks
	UserC controller;
	
	@Mock
	UserService service;
	
	@Mock
	EmailService email;
	
	@Before
	public void setUpController(){
		mockMvc = MockMvcBuilders.standaloneSetup(controller).setViewResolvers(viewResolver()).build();
	}
	
	@Test
	public void shouldReturnLoginForm() throws Exception{
		mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(view().name("user_form"))
        .andExpect(model().attributeExists("user"));
	}
	
	@Test
	public void shouldReturnMainPage() throws Exception{
		mockMvc.perform(get("/main"))
		.andExpect(status().isOk())
		.andExpect(view().name("main"));
	}
	
	@Test
	public void shouldReturnChangePasswordForm() throws Exception{
		mockMvc.perform(get("/change_password_form"))
		.andExpect(status().isOk())
		.andExpect(view().name("change_password_form"))
		.andExpect( model().attributeExists("request") );
	}
	
	
	@Test
	public void changePasswordShouldRedirectWithoutUser() throws Exception{
		mockMvc.perform(post("/change_password")
				.param("old", "password")
				.param("new1", "password1")
				.param("new2", "password1"))
				.andExpect(redirectedUrl("/"));
		verify(service).currentUser();
	}
	
	
	
	@Test
	public void shouldEmailAndRedirectOnPasswordChange() throws Exception{
		Mockito.when(service.currentUser()).thenReturn(
				new User("as@ss.ss", BCrypt.hashpw("password", BCrypt.gensalt()))
				);
		mockMvc.perform(post("/change_password")
				.param("old", "password")
				.param("new1", "password1")
				.param("new2", "password1"))
				.andExpect(redirectedUrl("/logout"));
		verify(service).currentUser();
		verify(email).send("as@ss.ss", "Your password has been changed!", "Please contact your administrator if you did not change it");
	}
	
	
	@Test
	public void shouldRejectIfCurrentPasswordIsWrong() throws Exception{
		Mockito.when(service.currentUser()).thenReturn(
				new User("as@ss.ss", BCrypt.hashpw("password", BCrypt.gensalt()))
				);
		mockMvc.perform(post("/change_password")
				.param("old", "wrong")
				.param("new1", "password1")
				.param("new2", "password1"))
				.andExpect(redirectedUrl("/"));
	}
	
	
	@Test
	public void shouldRejectIfNotSignedIn() throws Exception{
		Mockito.when(service.currentUser()).thenReturn(null);
		mockMvc.perform(post("/change_password")
				.param("old", "wrong")
				.param("new1", "password1")
				.param("new2", "password1"))
				.andExpect(redirectedUrl("/"));
	}
	
	
	@Test
	public void shouldRejectIfNewPasswordsDontMatch() throws Exception{
		Mockito.when(service.currentUser()).thenReturn(
				new User("as@ss.ss", BCrypt.hashpw("password", BCrypt.gensalt()))
				);		
		mockMvc.perform(post("/change_password")
				.param("old", "password")
				.param("new1", "password2")
				.param("new2", "password1"))
				.andExpect(view().name("change_password_form"));
	}
	
	
	/* Not the best approach(not thymeleaf view resolver), but simple and tests that the views are resolved.*/
	private InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("");
		viewResolver.setSuffix(".html");
		return viewResolver;
	}

}


