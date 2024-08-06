package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class IndexController {
	
	@GetMapping("/")
	public String demo() {
		return "/index";
	}	
	
	@GetMapping("/index")
	public String index() {
		return "/index";
	}	
	
	@GetMapping("/loginPage")
	public String loginPage(HttpSession session) {
		Object sessionMember = session.getAttribute("member");
		if(sessionMember != null) {
			return "/index";
		}
		return "/loginPage";
	}
	
	@GetMapping("/loginPageAJAX")
	public String loginPageAJAX(HttpSession session) {
		Object sessionMember = session.getAttribute("member");
		if(sessionMember != null) {
			return "/index";
		}
		return "/loginPageAJAX";
	}
	
	@PostMapping("/logout")
	public String logout(Model model, HttpSession session) {
		if(session != null) {
			session.invalidate();
		    return "redirect:/";
		}
		
		return "redirect:/";
	}
	
}