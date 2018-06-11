package com.zhilutec;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAutoConfiguration
@EnableAsync
@EnableKafka
@MapperScan(basePackages = "com.zhilutec.db.daos")
public class Consumer {
    public static void main(String[] args) {
        SpringApplication.run(Consumer.class, args);
    }

}
