package com.zhilutec.kafka.consumer;

import com.zhilutec.dbs.entities.Coordinate;
import com.zhilutec.dbs.entities.Warning;
import com.zhilutec.kafka.producer.KafkaProducerListener;
import com.zhilutec.services.IKafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


public class KafkaConsumerBatchListener {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.kafka.producer.topic1}")
    private String coordinateTopic;

    @Value("${spring.kafka.producer.topic2}")
    private String alarmTopic;

    @Value("${spring.kafka.consumer.topic}")
    private String consumerTopic;

    @Resource
    private IKafkaService kafkaService;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 批量处理
     */
    @KafkaListener(topics = "${spring.kafka.consumer.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void listener(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        logger.info("--------consumer-批量Poll到的数组大小:" + records.size() + "-------");
        for (ConsumerRecord<String, String> record : records) {
            logger.info(record.value());
        }
        //debug
        kafkaTemplate.setProducerListener(new KafkaProducerListener());
        try {
            Map<String, Object> msgMap = kafkaService.handlePoints(records);
            if (msgMap != null) {
                List<Coordinate> coordinates = (List<Coordinate>) msgMap.get("coordinates");
                if (coordinates != null && coordinates.size() > 0) {
                    kafkaTemplate.send(coordinateTopic, coordinates.toString());
                }


                List<Warning> warnings = (List<Warning>) msgMap.get("warnings");
                if (warnings != null && warnings.size() > 0) {
                    kafkaTemplate.send(alarmTopic, warnings.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ack.acknowledge();//手动提交偏移量
        }
    }

}