package com.zhilutec.uwb.service;


import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

public interface IKafkaService {
    void batchSave(List<ConsumerRecord<String, String>> records);
}
