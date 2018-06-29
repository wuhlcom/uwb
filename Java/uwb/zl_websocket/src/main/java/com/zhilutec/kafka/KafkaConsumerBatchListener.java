package com.zhilutec.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.services.IKafkaService;
import com.zhilutec.services.UwbWebSocket;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import javax.annotation.Resource;
import javax.websocket.Session;
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
        logger.info("========================批量Poll到的数组大小:" + records.size() + "========================");
        for (ConsumerRecord<String, String> record : records) {
            logger.info("consumer value:{}", record.value());
        }

        try {
            Map<String, List<JSONObject>> msgMap = kafkaService.kafkaMsg(records);
            Map<Session, String> sessionMap = UwbWebSocket.getSessionMap();
            if (msgMap != null && !msgMap.isEmpty()) {
                for (Map.Entry<Session, String> entry : sessionMap.entrySet()) {
                    // 获取当前websocket session对象
                    Session wsSession = entry.getKey();
                    // 防止endpoint报错增加同步锁
                    // java.lang.IllegalStateException: The remote endpoint was in state [TEXT_FULL_WRITING] which is an invalid state for called method
                    synchronized (wsSession) {
                        if (wsSession.isOpen()) {
                            String wsMsg = entry.getValue().trim();
                            JSONObject wsMsgObj = JSON.parseObject(wsMsg);
                            // 请求类型
                            Integer wsType = wsMsgObj.getInteger("type");
                            if (wsType.intValue() == 1) {
                                List<JSONObject> coordinates = msgMap.get("coordinates");
                                if (coordinates != null && coordinates.size() > 0) {
                                    String coordinateStr = Result.ok(coordinates).toJSONString();
                                    kafkaService.wsSendMsgBase(wsSession, coordinateStr);
                                }
                            } else if (wsType.intValue() == 2) {
                                List<JSONObject> warnings = msgMap.get("warnings");
                                if (warnings != null && warnings.size() > 0) {
                                    String warningsStr = Result.ok(warnings).toJSONString();
                                    kafkaService.wsSendMsgBase(wsSession, warningsStr);
                                }
                            }
                        } else {
                            sessionMap.remove(wsSession);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ack.acknowledge();//手动提交偏移量
        }
    }

}