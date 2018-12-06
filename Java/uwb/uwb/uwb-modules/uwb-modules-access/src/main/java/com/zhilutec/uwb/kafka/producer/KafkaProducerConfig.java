package com.zhilutec.uwb.kafka.producer;

import com.mia.common.constant.ServiceConstant;
import com.zhilutec.uwb.service.impl.ZkServiceImpl;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerConfig.class);

    @Resource
    private KafkaProducerProperty kafkaProducerProperty;

    @Resource
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private ZkServiceImpl ZkService;

    @Bean
    public ProducerFactory<String, String> producerFactory() throws Exception {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() throws Exception {
        Map<String, Object> props = new HashMap<>();
        //+++++++++++++++++++++++++++++++++
        //等待连接 成功
        // int i = 0;
        // while (null == System.getProperty(ServiceConstant.REGISTRY_RESULT_ENV)) {
        //     logger.info("Waiting for kafka dynamic ip address...");
        //     i++;
        //     Thread.sleep(1000);
        //     if (i >= 30) {
        //         throw new Exception("动态获取kafka地址失败!");
        //     }
        // }
        // String server = System.getProperty(ServiceConstant.CONSUMER_KAFKA);
        //+++++++++++++++++++++++++++++++++

        //动态获取kafka地址
        String server = System.getProperty(ServiceConstant.CONSUMER_KAFKA);
        if (null == server) {
            server = kafkaProducerProperty.getBootstrapServers();
            logger.info("Get kafka sever " + server);
        } else {
            // server = kafkaConsumerProperty.getBootstrapServers();
            logger.info("Get kafka sever " + server);
        }

        logger.info("Init producer with " + server);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        // props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaProducerProperty.getBootstrapServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProducerProperty.getClientId());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerProperty.getAcks());
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProducerProperty.getBufferMemory());
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, kafkaProducerProperty.getCompressionType());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerProperty.getBatchSize());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerProperty.getLingerMs());
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerProperty.getRetries());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() throws Exception {
        return new KafkaTemplate<String, String>(producerFactory());
    }

}