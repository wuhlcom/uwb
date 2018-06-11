package com.zhilutec.kafka;

import com.zhilutec.services.IWebsocketService;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import javax.annotation.Resource;
import java.util.List;


public class KafkaConsumerBatchListener {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IWebsocketService websocketService;

    /**
     * 批量保存
     */
    @KafkaListener(topics = KafkaConfig.KAFKA_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void listen(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        logger.info("---------批量Poll到的数据大小-----------------" + records.size());
        try {
            // msgStorageService.saveDataBatch(records);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ack.acknowledge();//手动提交偏移量
        }
    }
}