package com.stepan.web.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stepan.model.User;
import com.stepan.service.UserService;
import com.stepan.service.email.EmailService;

@Controller
@RequestMapping("/admin")
public class AdminC {
	
	@Autowired
	UserService userService;
	
	@Autowired
	EmailService emailService;
	
	@RequestMapping(value = "/reset_pass_user/{id}", method = RequestMethod.GET)
    public String resetPassword (@PathVariable("id") Long id, Model model) {
		String tempPassword = UUID.randomUUID().toString().replaceAll("-", "");
		User user = userService.byId(id);
		user.setPassword(tempPassword);
		userService.rehashPassword(user);
		emailService.send(user.getUsername(), "Your password has been reset", "Use the following temporary password: "+ tempPassword);
		return "redirect:/admin/main";
	}
	
	@RequestMapping(value = "/delete_user/{id}", method = RequestMethod.GET)
    public String delete (@PathVariable("id") Long id, Model model) {
		System.out.println(admin(id));
		if(! admin(id)){
			userService.delete(id);
		}
		return "redirect:/admin/main";
	}

	private boolean admin(Long id) {
		return userService
				.byId(id)
				.getRole()
				.getName().equalsIgnoreCase("ROLE_ADMIN");
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String save(@Valid User candidate, Errors errors, RedirectAttributes model) {
		String tempPassword = UUID.randomUUID().toString().replaceAll("-", "");
		candidate.setPassword(tempPassword);
		
		userService.create(candidate, "ROLE_USER");
		
		emailService.send(candidate.getUsername(), "Your account is ready", "Use the following temporary password: "+ tempPassword);
		return "redirect:/admin/main";
	}

	
	@RequestMapping("/main")
	public String main(Model m){
		m.addAttribute("user", new User());
		m.addAttribute("users", userService.findAll());
		return "admin/main";
	}

}
