package com.zhilutec.kafka.producer;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;

/**
 * kafkaProducer监听器，isInterestedInSuccess true则开启,用于调试
 *
 * @author
 */
public class KafkaProducerListener implements ProducerListener {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 发送消息成功后调用
     */
    @Override
    public void onSuccess(String topic, Integer partition, Object key,
                          Object value, RecordMetadata recordMetadata) {
        // logger.info("==========kafka发送数据成功（日志开始）==========");
        // logger.info("----------topic:" + topic);
        // logger.info("----------partition:" + partition);
        // logger.info("----------key:" + key);
        // logger.info("----------value:" + value);
        // logger.info("----------RecordMetadata:" + recordMetadata);
        // logger.info("==========kafka发送数据成功（日志结束）==========");
        logger.info("======producer=====onSuccess===============");
        logger.info("------producer value:{}", value);
    }

    /**
     * 发送消息错误后调用
     */
    @Override
    public void onError(String topic, Integer partition, Object key,
                        Object value, Exception exception) {
        //logger.info("==========kafka发送数据错误（日志开始）==========");
        //logger.info("----------topic:" + topic);
        //logger.info("----------partition:" + partition);
        //logger.info("----------key:" + key);
        //logger.info("----------value:" + value);
        //logger.info("----------Exception:" + exception);
        //logger.info("==========kafka发送数据错误（日志结束）==========");
        logger.info("======producer=====onError===============");
        logger.info("------Exception:" + exception);
        exception.printStackTrace();
    }

    /**
     * 方法返回值代表是否启动kafkaProducer监听器,true启动，false不启动
     */
    @Override
    public boolean isInterestedInSuccess() {
        // logger.info("/////////////////kafkaProducer监听器启动 ////////////////");
        return true;
    }

}