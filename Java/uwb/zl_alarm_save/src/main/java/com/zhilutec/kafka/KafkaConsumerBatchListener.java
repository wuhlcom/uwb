package com.zhilutec.kafka;

import com.zhilutec.dbs.entities.Warning;
import com.zhilutec.services.IKafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


public class KafkaConsumerBatchListener {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IKafkaService kafkaService;

    /**
     * 批量处理
     */
    @KafkaListener(topics = "${spring.kafka.consumer.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void listener(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        // logger.info("===================批量Poll到的数组大小:" + records.size() + "===================");
        // for (ConsumerRecord<?, ?> record : records) {
        //     System.out.println(record.value());
        // }
        try {
            kafkaService.batchSave(records);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ack.acknowledge();//手动提交偏移量
        }
    }

}