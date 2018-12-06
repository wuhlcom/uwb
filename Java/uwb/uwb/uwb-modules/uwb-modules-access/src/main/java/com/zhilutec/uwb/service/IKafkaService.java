package com.zhilutec.uwb.service;



import com.zhilutec.uwb.common.pojo.AlarmInfo;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

public interface IKafkaService {


    AlarmInfo getAlarmStatus(List<ConsumerRecord<String, String>> records);
}
