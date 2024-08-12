package com.example.demo.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.Member;
import com.example.demo.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LoginFilter implements Filter{
	
	private static final Set<String> allowedPath = Set.of(
			"/", "/index", "/loginPage", "/loginPageAJAX", "/css/myStyle.css", 
			"/images/Minions.gif", "/images/redPikmin2.gif", "/js/loginAJAX.js"); 

	@Autowired
	private MemberService memberService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		System.out.println("Filter is working...");
		
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		
		String requestPath = httpRequest.getServletPath();
		
		HttpSession session = httpRequest.getSession();
		Object sessionMember = session.getAttribute("member");
		
		if(allowedPath.contains(requestPath) || sessionMember != null) {
			chain.doFilter(httpRequest, httpResponse);
		}else {
			
			switch(requestPath) {
				case "/member/login":		
					doLogin(httpRequest, httpResponse, session, chain);
					break;
				case "/member/loginAJAX":
					doLoginAJAX(httpRequest, httpResponse, response, chain);
		            break;
				default:
					session.setAttribute("errorMsg", "請登入會員");
					httpResponse.sendRedirect(httpRequest.getContextPath() + "/loginPage");
			}
		}

	}

	@Override
	public void destroy() {
		Filter.super.destroy();
	}
	
	
	
	public void doLogin(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
													HttpSession session, FilterChain chain) throws IOException, ServletException{
		String username = httpRequest.getParameter("username");
		String password = httpRequest.getParameter("password");
		
		Member member = memberService.getOneMember(username);
		if (member == null) {
			session.setAttribute("errorMsg", "查無此會員帳號");
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/loginPage");
			return;
		}else {
			String memberPassword = member.getPassword();
			if(!(memberPassword.equals(password))) {
				session.setAttribute("errorMsg", "帳號密碼錯誤，請重新輸入");
				httpResponse.sendRedirect(httpRequest.getContextPath() + "/loginPage");
				return;
			}else {
				httpRequest.setAttribute("username", username);
				chain.doFilter(httpRequest, httpResponse);
			}			
		}
		
	}
	
	
	public void doLoginAJAX(HttpServletRequest httpRequest, HttpServletResponse httpResponse, 
				ServletResponse response, FilterChain chain) throws IOException, ServletException{
		
		ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> jsonMap = objectMapper.readValue(httpRequest.getInputStream(), Map.class);
        
        String usernameAJAX = jsonMap.get("username");
        String passwordAJAX = jsonMap.get("password");
        
        if(usernameAJAX == "" || passwordAJAX == "") {
        	response.getWriter().write("");
			return;
		}
        
        Member memberAJAX = memberService.getOneMember(usernameAJAX);
        if (memberAJAX == null) {
        	response.getWriter().write("null");
			return;
		}else {
			
			String memberPasswordAJAX = memberAJAX.getPassword();
			if(!(memberPasswordAJAX.equals(passwordAJAX))) {
				response.getWriter().write("fail");
				return;
			}else {
				httpRequest.setAttribute("username", usernameAJAX);
				chain.doFilter(httpRequest, httpResponse);
			}			
		}
		
	}
	
}
