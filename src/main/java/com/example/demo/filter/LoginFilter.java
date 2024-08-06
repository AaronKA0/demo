package com.example.demo.filter;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.example.demo.model.Member;
import com.example.demo.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginFilter implements Filter{
	
	@Autowired
	private MemberService memberService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Filter.super.init(filterConfig);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
	            								filterConfig.getServletContext());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		String requestURI = httpRequest.getRequestURI();
		
		HttpSession session = httpRequest.getSession();
		Object sessionMember = session.getAttribute("member");
		
		System.out.println("Filter is working...");
		
		// ---------------------------- AJAX ----------------------------
		
		boolean isLoginRequest = requestURI.equals("/demo/member/loginAJAX");
		
		if(isLoginRequest) {
			ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> jsonMap = objectMapper.readValue(httpRequest.getInputStream(), Map.class);
            
            String username = jsonMap.get("username");
            String password = jsonMap.get("password");
            
            Integer comparison = memberService.checkPassword(username, password);
            
			if(comparison == 1) {
				httpRequest.setAttribute("username", username);
				chain.doFilter(httpRequest, httpResponse);
				return;
			}else{
				response.getWriter().write(Integer.toString(comparison));
				return;
			}
		}
		// ------------------------- END AJAX ----------------------------
		
		
		if(sessionMember != null) {
			chain.doFilter(httpRequest, httpResponse);
			return;
		}
				
		if(requestURI.endsWith("/login")) {
			String username = httpRequest.getParameter("username");
			String password = httpRequest.getParameter("password");

			if(username == null || password == null) {
				session.setAttribute("errorMsg", "請登入會員");
				httpResponse.sendRedirect(httpRequest.getContextPath() + "/loginPage");
				return;
			}
			
			Integer comparison = memberService.checkPassword(username, password);
			switch(comparison) {
				case 1:
					Member member = memberService.getOneMember(username);
					session.setAttribute("member", member);
					chain.doFilter(httpRequest, httpResponse);
					break;
				case -1:
					session.setAttribute("errorMsg", "帳號密碼錯誤，請重新輸入");
					httpResponse.sendRedirect(httpRequest.getContextPath() + "/loginPage");
					break;
				case 0:
					session.setAttribute("errorMsg", "查無此會員帳號");
				default:
					httpResponse.sendRedirect(httpRequest.getContextPath() + "/loginPage");
					break;
			}
			
		}else {
			session.setAttribute("errorMsg", "請登入會員");
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/loginPage");
			return;
		}
			
	}

	@Override
	public void destroy() {
		Filter.super.destroy();
	}

}
