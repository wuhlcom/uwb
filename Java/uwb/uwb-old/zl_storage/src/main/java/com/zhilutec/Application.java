package com.zhilutec;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableAsync
@EnableKafka
@EnableTransactionManagement
@MapperScan(basePackages = "com.zhilutec.dbs.daos")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
