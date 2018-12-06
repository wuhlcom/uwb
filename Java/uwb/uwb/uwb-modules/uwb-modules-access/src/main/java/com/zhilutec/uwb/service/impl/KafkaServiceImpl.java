package com.zhilutec.uwb.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.zhilutec.uwb.common.pojo.AlarmInfo;
import com.zhilutec.uwb.service.IKafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.zhilutec.uwb.util.ConstantUtil;

import java.util.List;
import java.util.Map;


/**
 * 从kafka获取消息，判断是否如果有报警消息
 */
@Service
public class KafkaServiceImpl implements IKafkaService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public AlarmInfo getAlarmStatus(List<ConsumerRecord<String, String>> records) {
        AlarmInfo alarmInfo = new AlarmInfo();
        for (ConsumerRecord<String, String> record : records) {
            Map<String, Object> map = JSONObject.parseObject(record.value().toString(), Map.class);
            Integer alarmStatus = (Integer) map.get("alarm");
            if (alarmStatus != null) {
                if (alarmStatus.intValue() == ConstantUtil.ALARM_ON.intValue() || alarmStatus.intValue() == ConstantUtil.ALARM_OFF.intValue()) {
                    alarmInfo.setSw(alarmStatus);
                    break;
                } else {
                    alarmInfo = null;
                }
            } else {
                alarmInfo = null;
            }
        }
        return alarmInfo;
    }

}
