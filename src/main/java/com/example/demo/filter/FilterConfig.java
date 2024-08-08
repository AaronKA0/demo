package com.example.demo.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
	
	@Autowired
	private LoginFilter loginFilter;
	
	@Bean
	public FilterRegistrationBean<LoginFilter> loginFilterMethod(){
		FilterRegistrationBean<LoginFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(loginFilter);
		registrationBean.addUrlPatterns("/*");
		return registrationBean;
	}

}
