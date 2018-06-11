package com.zhilutec.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{

	public WebMvcConfig() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registy) {
		registy.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
	}

}
