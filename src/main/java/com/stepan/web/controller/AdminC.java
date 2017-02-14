//package com.stepan.web.controller;
//
//import java.util.UUID;
//
//import javax.servlet.http.HttpSession;
//import javax.validation.Valid;
//
//import org.mindrot.jbcrypt.BCrypt;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.Errors;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.stepan.model.User;
//import com.stepan.service.EmailService;
//import com.stepan.service.UserService;
//
//@Controller
//@RequestMapping("/admin")
//public class AdminC {
//	
//	@Autowired
//	UserService userService;
//	
//	@Autowired
//	EmailService emailService;
//	
//	@Value("${adminEmail}")
//	String adminEmail;
//	
//	@Autowired
//	HttpSession session;
//	
//	@RequestMapping(value = "/create", method = RequestMethod.POST)
//	public String save(@Valid User candidate, Errors errors, RedirectAttributes model) {
//		String tempPassword = UUID.randomUUID().toString().replaceAll("-", "");
//		candidate.setPassword(tempPassword);
//		userService.create(candidate);
//		emailService.send(candidate.getUserName(), "Your account is ready", "Use the following temporary password: "+ tempPassword);
//		return "redirect:/admin/main";
//	}
//	
//	@RequestMapping(value = "/reset_pass_user/{id}", method = RequestMethod.GET)
//    public String resetPassword (@PathVariable("id") Long id, Model model) {
//		String tempPassword = UUID.randomUUID().toString().replaceAll("-", "");
//		User user = userService.byId(id);
//		user.setPassword(tempPassword);
//		emailService.send(user.getUserName(), "Your password has been reset", "Use the following temporary password: "+ tempPassword);
//		return "redirect:/admin/main";
//	}
//	
//	@RequestMapping(value = "/delete_user/{id}", method = RequestMethod.GET)
//    public String delete (@PathVariable("id") Long id, Model model) {
//		if(! userService.byId(id).getUserName().equalsIgnoreCase(adminEmail)) userService.delete(id);
//		return "redirect:/admin/main";
//	}
//	
//
//	@RequestMapping("/login_form")
//	public String login(Model m){
//		m.addAttribute("user", new User());
//		return "admin/login_form";
//	}
//	
//	@RequestMapping(value = "/login", method = RequestMethod.POST)
//	public String login(@Valid User candidate, Errors errors, RedirectAttributes model) {		
//		if(errors.hasErrors()) return "admin/login_form";		
//		User existing = userService.byUserName(candidate.getUserName());
//		// sign in attempt with valid password
//		if(		existing != null 
//				&& BCrypt.checkpw(candidate.getPassword(), existing.getPassword())
//				&& existing.getUserName().equalsIgnoreCase(adminEmail)){
//			session.setAttribute("adminEmail", existing.getUserName());	
//			return "redirect:/admin/main";
//		}
//		return "redirect:/admin/login_form";	
//	}
//	
//	@RequestMapping("/main")
//	public String main(Model m){
//		m.addAttribute("user", new User());
//		m.addAttribute("users", userService.findAll());
//		return "admin/main";
//	}
//
//}
