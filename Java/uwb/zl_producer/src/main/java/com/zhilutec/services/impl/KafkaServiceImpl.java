package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.pojos.AlarmInfo;
import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.services.IKafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class KafkaServiceImpl implements IKafkaService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public AlarmInfo getAlarmStatus(List<ConsumerRecord<String, String>> records) {
        AlarmInfo alarmInfo = new AlarmInfo();
        for (ConsumerRecord<String, String> record : records) {
            Map<String, Object> map = JSONObject.parseObject(record.value().toString(), Map.class);
            Integer alarmStatus = (Integer) map.get("alarm");
            if (alarmStatus.intValue() == ConstantUtil.ALARM_ON.intValue() || alarmStatus.intValue() == ConstantUtil.ALARM_OFF.intValue()) {
                alarmInfo.setSw(alarmStatus);
                break;
            } else {
                alarmInfo = null;
            }
        }
        return alarmInfo;
    }

}
