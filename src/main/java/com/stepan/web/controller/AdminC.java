package com.stepan.web.controller;

import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stepan.model.Role;
import com.stepan.model.User;
import com.stepan.service.EmailService;
import com.stepan.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminC {
	
	@Autowired
	UserService userService;
	
	@Autowired
	EmailService emailService;
	
	@Value("${adminEmail}")
	String adminEmail;
	
	@RequestMapping(value = "/reset_pass_user/{id}", method = RequestMethod.GET)
    public String resetPassword (@PathVariable("id") Long id, Model model) {
		String tempPassword = UUID.randomUUID().toString().replaceAll("-", "");
		System.out.println(tempPassword);
		User user = userService.byId(id);
		user.setPassword(tempPassword);
		emailService.send(user.getUserName(), "Your password has been reset", "Use the following temporary password: "+ tempPassword);
		return "redirect:/admin/main";
	}
	
	@RequestMapping(value = "/delete_user/{id}", method = RequestMethod.GET)
    public String delete (@PathVariable("id") Long id, Model model) {
		if(! userService.byId(id).getUserName().equalsIgnoreCase(adminEmail)) userService.delete(id);
		return "redirect:/admin/main";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String save(@Valid User candidate, Errors errors, RedirectAttributes model) {
		String tempPassword = UUID.randomUUID().toString().replaceAll("-", "");
		System.out.println(tempPassword);
		// TODO MOVE LINKING LOGIC (INCLUDING SAVING) TO SERVICE
		candidate.setPassword(tempPassword);
		userService.create(candidate);
		
		Role role = userService.saveRole(new Role("ROLE_USER"));
		candidate.linkNewRole(role);
		userService.saveRole(role); // saves link
		
		emailService.send(candidate.getUserName(), "Your account is ready", "Use the following temporary password: "+ tempPassword);
		return "redirect:/admin/main";
	}

	
	@RequestMapping("/main")
	public String main(Model m){
		m.addAttribute("user", new User());
		m.addAttribute("users", userService.findAll());
		return "admin/main";
	}

}
