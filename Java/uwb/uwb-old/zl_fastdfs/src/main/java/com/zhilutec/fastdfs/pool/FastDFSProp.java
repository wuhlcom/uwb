package com.zhilutec.fastdfs.pool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FastDFSProp {

    public FastDFSProp() {
    }

    // fastdfs.tracker_servers=192.168.10.232:22122
    @Value("${fastdfs.tracker_servers}")
    private String trackerServers;

    // fastdfs.connect_timeout_in_seconds=120
    @Value("${fastdfs.connect_timeout_in_seconds}")
    private Integer connTimeout;

    // fastdfs.network_timeout_in_seconds=130
    @Value("${fastdfs.network_timeout_in_seconds}")
    private Integer netTimeout;

    // fastdfs.charset=UTF-8
    @Value("${fastdfs.charset}")
    private String charset;

    // fastdfs.http_anti_steal_token=no
    @Value("${fastdfs.http_anti_steal_token}")
    private String stealToken;

    // fastdfs.http_secret_key=FastDFS1234567890
    @Value("${fastdfs.http_secret_key}")
    private String secretKey;

    // fastdfs.http_tracker_http_port=8888
    @Value("${fastdfs.http_tracker_http_port}")
    private String trackerHttpPort;

    @Value("${fastdfs.pool.minPoolSize}")
    private long minPoolSize;

    @Value("${fastdfs.pool.waitTimes}")
    private long waitTimes;

    @Value("${fastdfs.pool.heartRatio}")
    private long heartRatio;

    public String getTrackerServers() {
        return trackerServers;
    }

    public void setTrackerServers(String trackerServers) {
        this.trackerServers = trackerServers;
    }

    public Integer getConnTimeout() {
        return connTimeout;
    }

    public void setConnTimeout(Integer connTimeout) {
        this.connTimeout = connTimeout;
    }

    public Integer getNetTimeout() {
        return netTimeout;
    }

    public void setNetTimeout(Integer netTimeout) {
        this.netTimeout = netTimeout;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getStealToken() {
        return stealToken;
    }

    public void setStealToken(String stealToken) {
        this.stealToken = stealToken;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTrackerHttpPort() {
        return trackerHttpPort;
    }

    public void setTrackerHttpPort(String trackerHttpPort) {
        this.trackerHttpPort = trackerHttpPort;
    }

    public long getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(long minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public long getWaitTimes() {
        return waitTimes;
    }

    public void setWaitTimes(long waitTimes) {
        this.waitTimes = waitTimes;
    }

    public long getHeartRatio() {
        return heartRatio;
    }

    public void setHeartRatio(long heartRatio) {
        this.heartRatio = heartRatio;
    }

    @Override
    public String toString() {
        return "FastDFSProp{" +
                "trackerServers='" + trackerServers + '\'' +
                ", connTimeout=" + connTimeout +
                ", netTimeout=" + netTimeout +
                ", charset='" + charset + '\'' +
                ", stealToken='" + stealToken + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", trackerHttpPort='" + trackerHttpPort + '\'' +
                ", minPoolSize=" + minPoolSize +
                ", waitTimes=" + waitTimes +
                ", heartRatio=" + heartRatio +
                '}';
    }
}
