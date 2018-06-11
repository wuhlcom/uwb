package com.zhilutec.services;


import org.apache.kafka.clients.consumer.ConsumerRecord;

import javax.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IKafkaService {

    Map<String, Object> handlePoints(List<ConsumerRecord<String, String>> records) throws InterruptedException;
}
