package com.zhilutec;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;


@EnableAutoConfiguration
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "com.zhilutec.dbs.daos")
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        // 读出环境变量的值
        // Environment environment = context.getEnvironment();
        // String property = environment.getProperty("fastdfs.tracker_servers");
    }

    // /**
    //  * 文件上传配置
    //  * @return
    //  */
    // @Bean
    // public MultipartConfigElement multipartConfigElement() {
    //     MultipartConfigFactory factory = new MultipartConfigFactory();
    //     //文件最大
    //     factory.setMaxFileSize("10MB"); //KB,MB
    //     /// 设置总上传数据总大小
    //     factory.setMaxRequestSize("100MB");
    //     return factory.createMultipartConfig();
    // }
}
