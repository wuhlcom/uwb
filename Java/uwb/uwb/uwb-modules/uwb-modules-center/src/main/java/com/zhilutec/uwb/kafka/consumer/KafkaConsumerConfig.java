package com.zhilutec.uwb.kafka.consumer;

import com.mia.common.constant.ServiceConstant;
import com.zhilutec.uwb.service.ZkService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@EnableKafka
@ComponentScan
@Configuration
public class KafkaConsumerConfig {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    @Autowired
    private KafkaConsumerProperty kafkaConsumerProperty;

    @Resource
    private ZkService zkService;

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(kafkaConsumerProperty.getConcurrency()); //指定消费线程的数量
        factory.setBatchListener(kafkaConsumerProperty.getBatchListener());//设置为批量消费，每个批次数量在Kafka配置参数中设置ConsumerConfig.MAX_POLL_RECORDS_CONFIG
        factory.getContainerProperties().setPollTimeout(kafkaConsumerProperty.getPollTimeout());
        factory.getContainerProperties().setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL_IMMEDIATE);//设置提交偏移量的方式
        // factory.getContainerProperties().setAckTime(kafkaConfig.getAckTime());//AckMode为COUNT_TIME或TIME
        // factory.getContainerProperties().setAckCount(kafkaConfig.getAckCount());//AckMode为COUNT_TIME或COUNT是限制最多未提交offset的数量
        return factory;
    }

    @Bean
    public ConsumerFactory<Integer, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();

        //动态获取kafka地址
        String server = System.getProperty(ServiceConstant.CONSUMER_KAFKA);
        if (null == server) {
            server = kafkaConsumerProperty.getBootstrapServers();
            logger.info("Get kafka sever " + server);
        } else {
            // server = kafkaConsumerProperty.getBootstrapServers();
            logger.info("Get kafka sever " + server);
        }

        // propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerServerConfig.getBootstrapServers());//kafka server
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);//kafka server
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerProperty.getGroupId());//消费者组
        propsMap.put(ConsumerConfig.CLIENT_ID_CONFIG, kafkaConsumerProperty.getClientId());//消费者Id
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerProperty.getAutoOffsetReset());//自动提交重置offset的方式
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaConsumerProperty.getAutoCommit());//自动提交offset
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, kafkaConsumerProperty.getAutoCommitInterval());//自动提交间隔
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaConsumerProperty.getSessionTimeout());//consumer消费超时
        propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConsumerProperty.getMaxPollRecords()); //限制最大消费数量
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class); //key解码
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);//value解码
        return propsMap;
    }

    @Bean
    KafkaConsumerBatchListener batchListener() {
        return new KafkaConsumerBatchListener();
    }


}
