package com.zhilutec.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@ComponentScan()
@Configuration
public class KafkaProducerConfig {

    @Autowired
    KafkaConfig kafkaConfig;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaConfig.getClientId());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaConfig.getAcks());
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaConfig.getBufferMemory());
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, kafkaConfig.getCompressionType());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaConfig.getBatchSize());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaConfig.getLingerMs());
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaConfig.getRetries());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<String, String>(producerFactory());
    }

}