package com.zhilutec.netty.tcpServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 *
 * Created by whl
 */
//自定义spring boot事件监听
public class StartupEvent implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger log = LoggerFactory.getLogger(StartupEvent.class);

    private static ApplicationContext context;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {

            context = contextRefreshedEvent.getApplicationContext();
            TcpConfig tcpConfig = (TcpConfig) context.getBean(TcpConfig.class);
 
            //接收UDP消息并保存至redis和数据库中
            TcpServer tcpServer = (TcpServer) StartupEvent.getBean(TcpServer.class);
            tcpServer.run(tcpConfig.getPort());
            // 可以开启多个线程去执行不同的任务


        } catch (Exception e) {
            log.error("Exception", e);
        }
    }

    public static Object getBean(Class<TcpServer> beanName) {
        return context != null ? context.getBean(beanName) : null;
    }
}
