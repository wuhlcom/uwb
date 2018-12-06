package com.zhilutec.kafka.consumer;

import com.alibaba.fastjson.JSON;
import com.zhilutec.common.pojos.AlarmInfo;
import com.zhilutec.services.IKafkaService;
import com.zhilutec.services.IUpgradeService;
import io.netty.channel.Channel;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 从kafka获取消息
 * 如果有报警消息就向tcp连接发送报警通知
 */
@EnableKafka
public class KafkaConsumerBatchListener {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.kafka.producer.topic}")
    private String uwbCenterTopic;

    @Resource
    private IKafkaService kafkaService;

    @Resource
    private IUpgradeService upgradeService;



    /**
     * 批量处理
     */
    @KafkaListener(topics = "${spring.kafka.consumer.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void listener(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        logger.info("--------consumer-批量Poll到的数组大小:" + records.size() + "-------");
        // for (ConsumerRecord<String, String> record : records) {
        //     logger.info("-----consumer data:{}", record.value());
        // }

        try {
            AlarmInfo alarmInfo = kafkaService.getAlarmStatus(records);
            if (alarmInfo != null) {
                String jsonStr = JSON.toJSONString(alarmInfo);
                //获取tcp会话
                Map<String, Channel> concurrentHashMap = upgradeService.getActiveChannel();
                //通过tcp channel发送请求,获取版本信息
                for (Map.Entry<String, Channel> entry : concurrentHashMap.entrySet()) {
                    Channel channel = entry.getValue();
                    channel.writeAndFlush(jsonStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ack.acknowledge();//手动提交偏移量
        }
    }

}