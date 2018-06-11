package com.zhilutec.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@ComponentScan
@Configuration
public class KafkaConsumerConfig {

    @Autowired
    private ConsumerServerConfig consumerServerConfig;

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(consumerServerConfig.getConcurrency()); //指定消费线程的数量
        factory.setBatchListener(consumerServerConfig.getBatchListener());//设置为批量消费，每个批次数量在Kafka配置参数中设置ConsumerConfig.MAX_POLL_RECORDS_CONFIG
        factory.getContainerProperties().setPollTimeout(consumerServerConfig.getPollTimeout());
        factory.getContainerProperties().setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL_IMMEDIATE);//设置提交偏移量的方式
        // factory.getContainerProperties().setAckTime(consumerServerConfig.getAckTime());//AckMode为COUNT_TIME或TIME
        // factory.getContainerProperties().setAckCount(consumerServerConfig.getAckCount());//AckMode为COUNT_TIME或COUNT是限制最多未提交offset的数量
        return factory;
    }

    @Bean
    public ConsumerFactory<Integer, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);//kafka server
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, consumerServerConfig.getGroupId());//消费者组
        propsMap.put(ConsumerConfig.CLIENT_ID_CONFIG, consumerServerConfig.getClientId());//消费者Id
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerServerConfig.getAutoOffsetReset());//自动提交重置offset的方式
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumerServerConfig.getAutoCommit());//自动提交offset
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, consumerServerConfig.getAutoCommitInterval());//自动提交间隔
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, consumerServerConfig.getSessionTimeout());//consumer消费超时
        propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumerServerConfig.getMaxPollRecords()); //限制最大消费数量
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class); //key解码
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);//value解码
        return propsMap;
    }


    @Bean
    KafkaConsumerBatchListener batchListener() {
        return new KafkaConsumerBatchListener();
    }


}
