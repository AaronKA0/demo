package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Member;
import com.example.demo.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/member")
public class LoginController {
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/memberList")
	public String memberList(Model model, HttpSession session) {
		List<Member> memberList = memberService.getAll();
		model.addAttribute("memberList", memberList);
		return "/member/memberList";
	}
	
	@GetMapping("/login")
	public String getLogin() {
		return "/member/memberInfo";
	}
	
	@PostMapping("/login")
	public String postLogin() {
		return "/member/memberInfo";
	}
	
	@GetMapping("/memberInfo")
	public String memberInfo() {
		return "/member/memberInfo";
	}
	
	@PostMapping("/loginAJAX")
	@ResponseBody
    public String getLoginAJAX(HttpServletRequest request, HttpSession session) {
		String username = (String) request.getAttribute("username");
		Member member = memberService.getOneMember(username);
		session.setAttribute("member", member);
        return "success";
    }	
	
	
}
