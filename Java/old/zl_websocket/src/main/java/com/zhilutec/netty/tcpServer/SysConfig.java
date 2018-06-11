package com.zhilutec.netty.tcpServer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by whl.
 */
@Component
@ConfigurationProperties(prefix="sysfig")
public class SysConfig {
    private int tcpPort;//UDP消息接收端口

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

	public int getTcpPort() {
		return tcpPort;
	}

	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}

    
}