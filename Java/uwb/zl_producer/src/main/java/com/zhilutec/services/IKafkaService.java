package com.zhilutec.services;


import com.zhilutec.common.pojos.AlarmInfo;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;
import java.util.Map;

public interface IKafkaService {


    AlarmInfo getAlarmStatus(List<ConsumerRecord<String, String>> records);
}
