/** 
* @author :wuhongliang wuhongliang@zhilutec.com
* @version :2017年11月7日 下午4:13:12 * 
*/ 
package com.zhilutec.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


@Configuration
public class WebSocketConfig {
	@Bean
	public ServerEndpointExporter serverEndpointExporter(){
		return new ServerEndpointExporter();
	}
}
