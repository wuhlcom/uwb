package com.zhilutec.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Optional;

// 消费者组消费不同topic，暂未使用
public class KafkaConsumerGroup2 {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @KafkaListener(topics = {"test-topic"})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            logger.info("listen1 " + message);
        }
    }

    @KafkaListener(topics = { "task1" },group = "group1")
    public void task1(ConsumerRecord<?, ?> record) {
        System.out.println("这是"+" task1 的消费者");
        System.out.println("这是group1 topic task1 KafkaConsumer ---------->>>>>>>>:"+ (record));
        Object message = record.value();
        System.out.println("group1 topic task1 "+record.topic());
        System.out.println(message);
        System.out.println(record.key());
        System.out.println(record);
    }


    @KafkaListener(topics = { "gift" },group = "group1")
    public void gift(ConsumerRecord<String, String> record) {
        String key = record.key();
        String value = record.value();
        System.out.println("groupId1 kafka gift Consumer value:"+value);

    }

}
