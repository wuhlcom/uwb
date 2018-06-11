package com.zhilutec.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *spring.kafka.producer.listener=true
 *spring.kafka.producer.acks= all
 *spring.kafka.producer.bootstrapServers= 192.168.10.166:9092
 *spring.kafka.producer.bufferMemory= 33554432
 *spring.kafka.producer.clientId=producer-client
 *spring.kafka.producer.compressionType= none
 *spring.kafka.producer.batchSize= 16384
 *spring.kafka.producer.lingerMs = 0
 *spring.kafka.producer.retries= 2
 *spring.kafka.producer.defaultTopic=positions
 */
@Component
@Configuration
@PropertySource(value = "classpath:kafkaProducer.properties")
@ConfigurationProperties(prefix = "spring.kafka.producer")
public class KafkaProducerProperty {
    private Boolean listener;
    private String acks;
    private String bootstrapServers;
    private String bufferMemory;
    private String clientId;
    private String compressionType;
    private String batchSize;
    private String lingerMs;
    private String retries;
    private String defaultTopic;

    public Boolean getListener() {
        return listener;
    }

    public void setListener(Boolean listener) {
        this.listener = listener;
    }

    public String getAcks() {
        return acks;
    }

    public void setAcks(String acks) {
        this.acks = acks;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getBufferMemory() {
        return bufferMemory;
    }

    public void setBufferMemory(String bufferMemory) {
        this.bufferMemory = bufferMemory;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(String compressionType) {
        this.compressionType = compressionType;
    }

    public String getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    public String getLingerMs() {
        return lingerMs;
    }

    public void setLingerMs(String lingerMs) {
        this.lingerMs = lingerMs;
    }

    public String getRetries() {
        return retries;
    }

    public void setRetries(String retries) {
        this.retries = retries;
    }

    public String getDefaultTopic() {
        return defaultTopic;
    }

    public void setDefaultTopic(String defaultTopic) {
        this.defaultTopic = defaultTopic;
    }

    @Override
    public String toString() {
        return "KafkaConfig{" +
                "listener=" + listener +
                ", acks='" + acks + '\'' +
                ", bootstrapServers='" + bootstrapServers + '\'' +
                ", bufferMemory='" + bufferMemory + '\'' +
                ", clientId='" + clientId + '\'' +
                ", compressionType='" + compressionType + '\'' +
                ", batchSize='" + batchSize + '\'' +
                ", lingerMs='" + lingerMs + '\'' +
                ", retries='" + retries + '\'' +
                ", defaultTopic='" + defaultTopic + '\'' +
                '}';
    }
}
