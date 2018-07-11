package com.zhilutec.kafka.producer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * spring.kafka.producer.bootstrapServers=${kafka.ip}:${kafka.port}
 * spring.kafka.producer.topic=${kafka.producer.topic}
 * spring.kafka.producer.listener=true
 * spring.kafka.producer.acks=all
 * spring.kafka.producer.bufferMemory=33554432
 * spring.kafka.producer.clientId=uwb-center-producer
 * spring.kafka.producer.compressionType=gzip
 * spring.kafka.producer.batchSize=16384
 * spring.kafka.producer.lingerMs=10
 * spring.kafka.producer.retries=2
 * spring.kafka.producer.protocol=PLAINTEXTSASL/SASL/PLAINTEXT/SASL_SSL/SSL
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "spring.kafka.producer")
public class KafkaProducerProperty {
    private String bootstrapServers;
    private String topic;
    private Boolean listener;
    private String acks;
    private String bufferMemory;
    private String clientId;
    private String compressionType;
    private String batchSize;
    private String lingerMs;
    private String retries;

    private String protocol;

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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "KafkaProducerProperty{" +
                "listener=" + listener +
                ", acks='" + acks + '\'' +
                ", bootstrapServers='" + bootstrapServers + '\'' +
                ", bufferMemory='" + bufferMemory + '\'' +
                ", clientId='" + clientId + '\'' +
                ", compressionType='" + compressionType + '\'' +
                ", batchSize='" + batchSize + '\'' +
                ", lingerMs='" + lingerMs + '\'' +
                ", retries='" + retries + '\'' +
                ", topic='" + topic + '\'' +
                '}';
    }
}
