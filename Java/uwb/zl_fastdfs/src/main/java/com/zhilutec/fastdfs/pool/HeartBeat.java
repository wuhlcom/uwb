package com.zhilutec.fastdfs.pool;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class HeartBeat {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(HeartBeat.class);

    private FastDFSConnectionPool pool = null;

    @Value("${fastdfs.pool.waitTimes}")
    private long waitTimes;

    @Value("${fastdfs.pool.heartRatio}")
    private long heartRatio;

    public HeartBeat(FastDFSConnectionPool pool) {
        this.pool = pool;
    }


    /**
     * 定时执行任务，检测当前的空闲连接是否可用，如果不可用将从连接池中移除
     */
    public void beat() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                LOGGER.info("[TrackerServer heart beat]");
                LinkedBlockingQueue<TrackerServer> idleConnectionPool = pool.getIdleConnectionPool();
                LOGGER.info("IdleConnectionPool has size:" + idleConnectionPool.size());
                TrackerServer ts = null;
                for (int i = 0; i < idleConnectionPool.size(); i++) {
                    try {
                        ts = idleConnectionPool.poll(waitTimes, TimeUnit.SECONDS);
                        if (ts != null) {
                            org.csource.fastdfs.ProtoCommon.activeTest(ts.getSocket());
                            pool.release(ts);
                        }
                    } catch (Exception e) {
                        LOGGER.error("[TrackerServer heart beat][异常：当前连接已不可用将进行重新获取连接]");
                        e.printStackTrace();
                        pool.drop(ts);
                    }
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, heartRatio, heartRatio);
    }

}
