package com.zhilutec.kafka;

import com.zhilutec.services.impl.MsgStorageServiceImpl;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import javax.annotation.Resource;

// 多个消费者消费相同的主题
public class KafkaConsumerGroup1 {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    MsgStorageServiceImpl messageStroage;

    @KafkaListener(topics = {KafkaConfig.KAFKA_TOPIC})
    // @KafkaListener(topicPartitions={@TopicPartition(partitions=KafkaConfig.KAFKA_PARTITION,topic=KafkaConfig.KAFKA_TOPIC)})//接收partition 1
    public void listener1(ConsumerRecord<?, ?> record) {
        // logger.info("Consumer Topic 'test' key: " + record.key());
        // logger.info("Consumer Topic 'test' value:" + record.value());
        messageStroage.saveData(record);
    }

    @KafkaListener(topics = {KafkaConfig.KAFKA_TOPIC})
    public void listener2(ConsumerRecord<?, ?> record) {
        messageStroage.saveData(record);
    }

    @KafkaListener(topics = {KafkaConfig.KAFKA_TOPIC})
    public void listener3(ConsumerRecord<?, ?> record) {
        messageStroage.saveData(record);
    }

}
