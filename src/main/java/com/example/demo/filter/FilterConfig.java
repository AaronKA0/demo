package com.example.demo.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.service.MemberService;

@Configuration
public class FilterConfig {

	private final MemberService memberService;

    public FilterConfig(MemberService memberService) {
        this.memberService = memberService;
    }
	
	@Bean
	public FilterRegistrationBean<LoginFilter> loginFilter(){
		FilterRegistrationBean<LoginFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new LoginFilter(memberService));
		registrationBean.addUrlPatterns("/*");
		return registrationBean;
	}
}
