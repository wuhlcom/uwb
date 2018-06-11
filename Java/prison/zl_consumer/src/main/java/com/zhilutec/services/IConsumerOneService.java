package com.zhilutec.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface IConsumerOneService {
    // 保存原始消息到数据库，并会在状态表记录缺勤状态
    void saveMessage(Long tagId);
    void saveMessage(ConsumerRecord<?, ?> record);

}
