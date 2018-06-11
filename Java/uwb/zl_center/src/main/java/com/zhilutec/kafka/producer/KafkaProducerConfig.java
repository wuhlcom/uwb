package com.zhilutec.kafka.producer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@EnableKafka
@ComponentScan
@Configuration
public class KafkaProducerConfig {

    @Resource
    private ProducerServerConfig producerServerConfig;

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String pBootstrapServers;


    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,pBootstrapServers);
        // props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,producerServerConfig.getBootstrapServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, producerServerConfig.getClientId());
        props.put(ProducerConfig.ACKS_CONFIG, producerServerConfig.getAcks());
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, producerServerConfig.getBufferMemory());
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, producerServerConfig.getCompressionType());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, producerServerConfig.getBatchSize());
        props.put(ProducerConfig.LINGER_MS_CONFIG, producerServerConfig.getLingerMs());
        props.put(ProducerConfig.RETRIES_CONFIG, producerServerConfig.getRetries());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<String, String>(producerFactory());
    }

}