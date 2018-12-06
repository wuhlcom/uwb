package com.zhilutec.uwb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * tcp server 参数配置
 *tcp.port=8822
 *tcp.bossThreadCount=2
 *tcp.workerThreadCount=2
 *tcp.soKeepAlive=true
 *tcp.soReuseaddr=true
 *tcp.soBacklog=100
 *tcp.soTimeout=30000
 *tcp.soRcvbuf=1048576
 *tcp.soSndbuf=1048576
 */

@Component()
@PropertySource(value= "classpath:nettyServer.properties")
@ConfigurationProperties(prefix="tcp")
public class TcpConfig {
	public TcpConfig() {

	}

	private int port;
    private int bossThreadCount;
    private int workerThreadCount;
    private boolean soKeepAlive;
    private boolean soReuseaddr;
    private int soBacklog;
	private int soTimeout;
    private int soRcvbuf;
    private int soSndbuf;
    private boolean soBroadcast;
    private boolean nodelay;
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getBossThreadCount() {
		return bossThreadCount;
	}
	public void setBossThreadCount(int bossThreadCount) {
		this.bossThreadCount = bossThreadCount;
	}
	public int getWorkerThreadCount() {
		return workerThreadCount;
	}
	public void setWorkerThreadCount(int workerThreadCount) {
		this.workerThreadCount = workerThreadCount;
	}
	public boolean isSoKeepAlive() {
		return soKeepAlive;
	}
	public void setSoKeepAlive(boolean soKeepAlive) {
		this.soKeepAlive = soKeepAlive;
	}
	public boolean isSoReuseaddr() {
		return soReuseaddr;
	}
	public void setSoReuseaddr(boolean soReuseaddr) {
		this.soReuseaddr = soReuseaddr;
	}
	public int getSoBacklog() {
		return soBacklog;
	}
	public void setSoBacklog(int soBacklog) {
		this.soBacklog = soBacklog;
	}
	public int getSoTimeout() {
		return soTimeout;
	}
	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}
	public int getSoRcvbuf() {
		return soRcvbuf;
	}
	public void setSoRcvbuf(int soRcvbuf) {
		this.soRcvbuf = soRcvbuf;
	}
	public int getSoSndbuf() {
		return soSndbuf;
	}
	public void setSoSndbuf(int soSndbuf) {
		this.soSndbuf = soSndbuf;
	}
	public boolean isSoBroadcast() {
		return soBroadcast;
	}
	public void setSoBroadcast(boolean soBroadcast) {
		this.soBroadcast = soBroadcast;
	}
	public boolean isNodelay() {
		return nodelay;
	}
	public void setNodelay(boolean nodelay) {
		this.nodelay = nodelay;
	}

    
}
