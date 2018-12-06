package com.zhilutec.uwb.service;


import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;
import java.util.Map;

public interface IKafkaService {

    Map<String, Object> handlePoints(List<ConsumerRecord<String, String>> records) throws InterruptedException;
}
