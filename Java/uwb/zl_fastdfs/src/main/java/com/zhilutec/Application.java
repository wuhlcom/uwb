package com.zhilutec;

import com.zhilutec.fastdfs.pool.FastDFSProp;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;


@EnableAutoConfiguration
@SpringBootApplication
// @EnableEurekaClient
@MapperScan(basePackages = "com.zhilutec.dbs.daos")
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        // 读出环境变量的值
        // Environment environment = context.getEnvironment();
        // String property = environment.getProperty("fastdfs.tracker_servers");
    }
}
