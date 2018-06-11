package com.zhilutec.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.utils.ChangeCharsetUtil;
import com.zhilutec.db.entities.WarningStatusEntity;
import com.zhilutec.services.IWebsocketService;
import com.zhilutec.services.UwbWebSocket;
import com.zhilutec.services.impl.WebsocketServiceImpl;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.util.Map;
import java.util.Optional;


public class KafkaConsumerListener {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IWebsocketService websocketService;


    @KafkaListener(topics = {KafkaConfig.KAFKA_TOPIC})
    // @KafkaListener(topicPartitions={@TopicPartition(partitions=KafkaConfig.KAFKA_PARTITION,topic=KafkaConfig.KAFKA_TOPIC)})//接收partition 1
    public void listener(ConsumerRecord<?, ?> record) {
        // logger.info("Consumer Topic '"+KafkaConfig.KAFKA_TOPIC +"'  key: " + record.key());
        // logger.info("Consumer Topic '"+KafkaConfig.KAFKA_TOPIC +"' value:" + record.value());
        // Long timeMillis = System.currentTimeMillis();
        Optional<?> kakfaMsg = Optional.ofNullable(record.value());
        try {
            String msg = null;
            if (kakfaMsg.isPresent()) {
                ChangeCharsetUtil strChange = new ChangeCharsetUtil();
                Object message = kakfaMsg.get();
                msg = strChange.toUTF_8(message.toString());
            } else {
                return;
            }

            //将消息转为json对象
            JSONObject kafkaJson = JSON.parseObject(msg);
            //转换带坐标的消息
            String msgConvert = websocketService.convertZero(msg);
            //保存状态到redis中
            websocketService.saveStatus(msgConvert);
            //保存最后一次数据到Map中
            websocketService.saveMessage(msgConvert);
            Long kafkaTagId = kafkaJson.getLong("tag_id");
            if (kafkaTagId == null) {
                logger.info("-------------------kafka消息中找不到tag id--------------------");
            }

            Integer op = kafkaJson.getInteger("op");
            if (op != null && op == 0) {
                logger.info("------------------收到取消报警消息不推送数据-----------------------");
            } else {
                //获取内存中的坐信息
                WarningStatusEntity coordiante = WebsocketServiceImpl.COORDINATE_MAP.get(kafkaTagId);

                //获取websocket session
                Map<Session, String> sessionMap = UwbWebSocket.getSessionMap();
                for (Map.Entry<Session, String> entry : sessionMap.entrySet()) {
                    // 获取当前websocket session对象
                    Session wsSession = entry.getKey();
                    synchronized (wsSession) {
                        if (wsSession.isOpen()) {
                            String wsMsg = entry.getValue().trim();
                            JSONObject wsMsgObj = JSON.parseObject(wsMsg);

                            // 请求类型
                            Integer wsType = wsMsgObj.getInteger("type");
                            if (wsType == 1) {
                                websocketService.sendMultiAreaMsg(wsSession, wsMsgObj, coordiante);
                            } else if (wsType == 2) {
                                websocketService.sendAreaMsg(kafkaTagId, wsSession, wsMsgObj);
                            } else if (wsType == 3) {
                                websocketService.sendPrisonerMsg(kafkaTagId, wsSession, wsMsgObj, coordiante, kafkaJson);
                            }
                        } else {
                            sessionMap.remove(wsSession);
                        }
                    }
                }
            }

            // Long timeMillis2 = System.currentTimeMillis();
            // Long value = (timeMillis2 - timeMillis);
            // logger.warn("kafka consumer-websocket 收到消息时间:" + timeMillis.toString());
            // logger.warn("kafka consumer-websocket 推送消息完成时间:" + timeMillis2.toString());
            // logger.warn("kafka consumer-websocket 推送消息用时:" + value.toString()+"ms");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("[Kafka Consumer-Websocket 推送消息错误:]:" + e.getMessage());
        }

    }
}