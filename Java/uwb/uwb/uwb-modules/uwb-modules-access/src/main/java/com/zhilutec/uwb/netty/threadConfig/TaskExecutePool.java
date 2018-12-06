package com.zhilutec.uwb.netty.threadConfig;

import com.zhilutec.uwb.config.TaskThreadPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by whl.
 * 自定义线程池
 */
@Configuration
@EnableAsync
@ComponentScan("com.zhilutec.uwb.config")
public class TaskExecutePool {

    @Autowired
    private TaskThreadPoolConfig taskThreadPoolConfig;

    @Bean
    public Executor nettyServer() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(taskThreadPoolConfig.getCorePoolSize());
        executor.setMaxPoolSize(taskThreadPoolConfig.getMaxPoolSize());
        executor.setQueueCapacity(taskThreadPoolConfig.getQueueCapacity());
        executor.setKeepAliveSeconds(taskThreadPoolConfig.getKeepAliveSeconds());
        executor.setThreadNamePrefix("=*ZL-TCP*=-");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}