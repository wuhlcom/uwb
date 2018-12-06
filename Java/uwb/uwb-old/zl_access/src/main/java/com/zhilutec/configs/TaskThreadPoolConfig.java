package com.zhilutec.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by whl.
 * 线程池参数配置
 */
@Component
@PropertySource(value= "classpath:/threadPool.properties")
@ConfigurationProperties(prefix="threadPool")
public class TaskThreadPoolConfig {
    //线程池信息
    private int CorePoolSize;

    private int MaxPoolSize;

    private int KeepAliveSeconds;

    private int QueueCapacity;

    public int getCorePoolSize() {
        return CorePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        CorePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return MaxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        MaxPoolSize = maxPoolSize;
    }

    public int getKeepAliveSeconds() {
        return KeepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        KeepAliveSeconds = keepAliveSeconds;
    }

    public int getQueueCapacity() {
        return QueueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        QueueCapacity = queueCapacity;
    }

}