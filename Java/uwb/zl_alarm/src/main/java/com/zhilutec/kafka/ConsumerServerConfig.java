package com.zhilutec.kafka;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * spring.kafka.consumer.autoOffsetReset=latest
 * spring.kafka.consumer.bootstrapServers=192.168.10.166:9092
 * spring.kafka.consumer.clientId=mysql-client
 * spring.kafka.consumer.autoCommit=false
 * spring.kafka.consumer.autoCommitInterval=100
 * spring.kafka.consumer.groupId=mysql
 * spring.kafka.consumer.maxPollRecords=5000
 * spring.kafka.consumer.ackCount=1000
 * spring.kafka.consumer.ackTime=2000
 * spring.kafka.consumer.concurrency=1
 * spring.kafka.consumer.pollTimeout=5000
 * spring.kafka.consumer.defaultTopic=positions
 * spring.kafka.consumer.batchListener=true
 * spring.kafka.consumer.sessionTimeout=15000
 */

@Component
@PropertySource(value = "classpath:kafkaConsumer.properties")
@ConfigurationProperties(prefix = "spring.kafka.consumer")
public class ConsumerServerConfig {


    private String autoOffsetReset;
    private String bootstrapServers;
    private String clientId;
    private Boolean autoCommit;
    private String autoCommitInterval;
    private String groupId;
    private String maxPollRecords;
    private Integer ackCount;
    private Long ackTime;
    private Integer concurrency;
    private Long pollTimeout;
    private String defaultTopic;
    private Boolean batchListener;
    private String sessionTimeout;

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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

    public String getMaxPollRecords() {
        return maxPollRecords;
    }

    public void setMaxPollRecords(String maxPollRecords) {
        this.maxPollRecords = maxPollRecords;
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

    public Long getPollTimeout() {
        return pollTimeout;
    }

    public void setPollTimeout(Long pollTimeout) {
        this.pollTimeout = pollTimeout;
    }

    public String getDefaultTopic() {
        return defaultTopic;
    }

    public void setDefaultTopic(String defaultTopic) {
        this.defaultTopic = defaultTopic;
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

    @Override
    public String toString() {
        return "KafkaConfig{" +
                "autoOffsetReset='" + autoOffsetReset + '\'' +
                ", bootstrapServers='" + bootstrapServers + '\'' +
                ", clientId='" + clientId + '\'' +
                ", autoCommit=" + autoCommit +
                ", autoCommitInterval='" + autoCommitInterval + '\'' +
                ", groupId='" + groupId + '\'' +
                ", maxPollRecords='" + maxPollRecords + '\'' +
                ", ackCount=" + ackCount +
                ", ackTime=" + ackTime +
                ", concurrency=" + concurrency +
                ", pollTimeout=" + pollTimeout +
                ", defaultTopic='" + defaultTopic + '\'' +
                ", batchListener=" + batchListener +
                ", sessionTimeout='" + sessionTimeout + '\'' +
                '}';
    }
}
