package com.zhilutec.uwb.kafka.consumer;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * spring.kafka.consumer.bootstrapServers=${kafka.ip}:${kafka.port}
 * spring.kafka.consumer.topic=${kafka.consumer.topic}
 * spring.kafka.consumer.autoOffsetReset=latest
 * spring.kafka.consumer.autoCommit=false
 * spring.kafka.consumer.autoCommitInterval=1000
 * spring.kafka.consumer.groupId=uwb-storage-save
 * spring.kafka.consumer.clientId=uwb-storage
 * spring.kafka.consumer.ackCount=1000
 * spring.kafka.consumer.ackTime=2000
 * spring.kafka.consumer.concurrency=5
 * spring.kafka.consumer.maxPollRecords=100
 * spring.kafka.consumer.maxPollIntervalMs=300000
 * spring.kafka.consumer.batchListener=true
 * spring.kafka.consumer.sessionTimeout=10000
 * spring.kakfa.consumer.fetchMinBytes=1
 * spring.kafka.consumer.fetchWaitMax=1000
 * spring.kafka.consumer.heartbeatIntervalMs=3000
 */

@Component
@ConfigurationProperties(prefix = "spring.kafka.consumer")
public class KafkaConsumerProperty {
    private String bootstrapServers;
    private String topic;

    private String autoOffsetReset;
    private Boolean autoCommit;
    private String autoCommitInterval;

    private String groupId;
    private String clientId;

    private Integer ackCount;
    private Long ackTime;

    private Integer concurrency;

    private String maxPollRecords;
    private Integer maxPollIntervalMs;
    private Long pollTimeout;

    private Boolean batchListener;
    private String sessionTimeout;
    private Integer fetchMinBytes;
    private Integer fetchWaitMax;
    private Integer heartbeatIntervalMs;

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public Boolean getAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(Boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public String getAutoCommitInterval() {
        return autoCommitInterval;
    }

    public void setAutoCommitInterval(String autoCommitInterval) {
        this.autoCommitInterval = autoCommitInterval;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Integer getAckCount() {
        return ackCount;
    }

    public void setAckCount(Integer ackCount) {
        this.ackCount = ackCount;
    }

    public Long getAckTime() {
        return ackTime;
    }

    public void setAckTime(Long ackTime) {
        this.ackTime = ackTime;
    }

    public Integer getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(Integer concurrency) {
        this.concurrency = concurrency;
    }

    public String getMaxPollRecords() {
        return maxPollRecords;
    }

    public void setMaxPollRecords(String maxPollRecords) {
        this.maxPollRecords = maxPollRecords;
    }

    public Integer getMaxPollIntervalMs() {
        return maxPollIntervalMs;
    }

    public void setMaxPollIntervalMs(Integer maxPollIntervalMs) {
        this.maxPollIntervalMs = maxPollIntervalMs;
    }

    public Boolean getBatchListener() {
        return batchListener;
    }

    public void setBatchListener(Boolean batchListener) {
        this.batchListener = batchListener;
    }

    public String getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(String sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public Integer getFetchMinBytes() {
        return fetchMinBytes;
    }

    public void setFetchMinBytes(Integer fetchMinBytes) {
        this.fetchMinBytes = fetchMinBytes;
    }

    public Integer getFetchWaitMax() {
        return fetchWaitMax;
    }

    public void setFetchWaitMax(Integer fetchWaitMax) {
        this.fetchWaitMax = fetchWaitMax;
    }

    public Integer getHeartbeatIntervalMs() {
        return heartbeatIntervalMs;
    }

    public void setHeartbeatIntervalMs(Integer heartbeatIntervalMs) {
        this.heartbeatIntervalMs = heartbeatIntervalMs;
    }

    public Long getPollTimeout() {
        return pollTimeout;
    }

    public void setPollTimeout(Long pollTimeout) {
        this.pollTimeout = pollTimeout;
    }

}
