package com.zhilutec.kafka;

import com.zhilutec.services.IMsgStorageService;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.kafka.support.Acknowledgment;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class KafkaConsumerListener  {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    IMsgStorageService msgStorageService;
    private CountDownLatch latch1 = new CountDownLatch(1);


    // /**逐条数据保存*/
    // @KafkaListener(topics = {KafkaConfig.KAFKA_TOPIC})
    // // @KafkaListener(topicPartitions={@TopicPartition(partitions=KafkaConfig.KAFKA_PARTITION,topic=KafkaConfig.KAFKA_TOPIC)})//接收指定partition 1
    // public void listener1(ConsumerRecord<?, ?> record) {
    //     // logger.info("Consumer Topic 'test' key: " + record.key());
    //     logger.info("Consumer Topic '" + KafkaConfig.KAFKA_TOPIC + "' value:" + record.value());
    //     logger.info("Consumer 初始化时间 :" + KafkaConsumerListener.INIT_TIME);    //
    //     msgStorageService.saveData(record);
    //     Long listenerTime = System.currentTimeMillis() / 1000;
    //     logger.info("Consumer 收到新消息时间 :" + listenerTime);
    //
    //     Long diff = listenerTime - KafkaConsumerListener.INIT_TIME;
    //     logger.info("Consumer 时间差 :" + diff);
    // }

    //批量保存使用自动提交或count或time超出范围机制
    // @KafkaListener(topics = KafkaConfig.KAFKA_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    // public void listen4(List<ConsumerRecord<?, ?>> records) {
    //     logger.info("---------批量Poll到的数据大小-----------------: " + records.size());
    //     msgStorageService.saveDataBatch(records);
    // }

    /**
     * 批量保存
     */
    @KafkaListener(topics = KafkaConfig.KAFKA_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void listen3(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) throws InterruptedException {
        logger.info("---------批量Poll到的数据大小-----------------" + records.size());
        try {
            msgStorageService.saveDataBatch(records);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ack.acknowledge();//手动提交偏移量
        }
    }

}