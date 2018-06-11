package com.zhilutec.netty.threadConfig;

import com.zhilutec.configs.TcpConfig;
import com.zhilutec.configs.async.AsyncTask;
import com.zhilutec.netty.tcpServer.TcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Created by whl
 * 自定义spring boot事件监听
 */
public class StartupEvent implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(StartupEvent.class);

    private static ApplicationContext context;

    public static <T> T getBean(Class<T> beanName) {
        return context != null ? context.getBean(beanName) : null;
    }

    public static <T> T getBean(String bean, Class<T> beanName) {
        return context != null ? context.getBean(bean, beanName) : null;
    }

    // 可以开启多个线程去执行不同的任务
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            context = contextRefreshedEvent.getApplicationContext();
            TcpConfig tcpConfig = context.getBean(TcpConfig.class);

            TcpServer tcpServer = StartupEvent.getBean(TcpServer.class);
            tcpServer.run(tcpConfig.getPort());

            // AsyncTask asyncTaskService = StartupEvent.getBean(AsyncTask.class);
            // for (int i = 0; i < 10; i++) {
            //     asyncTaskService.doTask1(i);
            // }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception", e);
        }
    }
}
