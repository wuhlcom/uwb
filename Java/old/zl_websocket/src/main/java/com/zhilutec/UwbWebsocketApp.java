package com.zhilutec;



import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@MapperScan(basePackages = "com.zhilutec.db.daos")
public class UwbWebsocketApp {	
	public static void main(String[] args) {
		SpringApplication.run(UwbWebsocketApp.class, args);
	}
	
}
