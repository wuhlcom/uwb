package com.zhilutec.configs.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * spring boot 启动监听类
 * 
 * @author liaokailin
 * @version $Id: MyApplicationStartedEventListener.java, v 0.1 2015年9月2日
 *          下午11:06:04 liaokailin Exp $
 */
public class Register {

	@Autowired
	private RequestListener requestListener;

	@Bean
	public ServletListenerRegistrationBean<RequestListener> servletListenerRegistrationBean() {
		ServletListenerRegistrationBean<RequestListener> servletListenerRegistrationBean = new ServletListenerRegistrationBean<>();
		servletListenerRegistrationBean.setListener(requestListener);
		return servletListenerRegistrationBean;
	}
}
