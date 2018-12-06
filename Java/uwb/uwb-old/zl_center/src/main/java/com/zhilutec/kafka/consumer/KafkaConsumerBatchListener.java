package com.zhilutec.kafka.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.dbs.entities.Coordinate;
import com.zhilutec.dbs.entities.Warning;
import com.zhilutec.kafka.producer.KafkaProducerListener;
import com.zhilutec.kafka.producer.KafkaProducerProperty;
import com.zhilutec.services.IKafkaService;
import com.zhilutec.services.ZkService;
import com.zhilutec.services.impl.ZkServiceImpl;
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

    @Value("${spring.kafka.producer.topic}")
    private String uwbCenterTopic;

    @Resource
    private IKafkaService kafkaService;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private ZkService zkService;

    /**
     * 批量处理
     */
    @KafkaListener(topics = "${spring.kafka.consumer.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void listener(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        logger.info("--------consumer-批量Poll到的数组大小:" + records.size() + "-------");
        // if (records.size()>0){
        //     logger.info("------打印一条数据-------");
        //     logger.info(records.get(0).value().toString());
        // }
        for (ConsumerRecord<String, String> record : records) {
            logger.info("-----consumer data:{}", record.value());
        }
        kafkaTemplate.setProducerListener(new KafkaProducerListener());
        try {
            Map<String, Object> msgMap = kafkaService.handlePoints(records);
            if (msgMap != null && !msgMap.isEmpty()) {
                String jsonStr = JSON.toJSONString(msgMap);
                // kafkaTemplate.send(uwbCenterTopic, jsonStr);
                zkService.sendToNeighbor(jsonStr, uwbCenterTopic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ack.acknowledge();//手动提交偏移量
        }
    }

}