package com.zhilutec.configs.async;

import com.zhilutec.services.ITcpHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

//多线程任务
@Component
public class AsyncTask {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    //myTaskAsynPool即配置线程池的方法名，此处如果不写自定义线程池的方法名，会使用默认的线程池
    @Async("myTaskAsyncPool")
    public void doTask1(int i) throws InterruptedException {
        logger.info("自定义任务" + i + " started.");
    }


}