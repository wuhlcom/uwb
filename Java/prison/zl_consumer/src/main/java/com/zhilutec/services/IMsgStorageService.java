package com.zhilutec.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

public interface IMsgStorageService {

    void saveData(ConsumerRecord<?, ?> record);

    void saveDataBatch(List<ConsumerRecord<?, ?>> records);
}
