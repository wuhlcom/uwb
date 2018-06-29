package com.zhilutec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
// @EnableEurekaClient
@EnableAsync
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        // 在main中添加多线程任务
        // AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AsyncTaskConfig.class);
        // AsyncTaskService asyncTaskService = context.getBean(AsyncTaskService.class);
        //
        // for(int i = 0; i < 10; i++){
        //     asyncTaskService.executeAsyncTask(i);
        //     asyncTaskService.executeAsyncTaskPlus(i);
        // }
    }

}
