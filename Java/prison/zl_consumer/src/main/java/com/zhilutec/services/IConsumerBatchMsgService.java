package com.zhilutec.services;

import com.zhilutec.db.entities.CoordinateEntity;
import com.zhilutec.db.entities.TagWarningEntity;
import com.zhilutec.db.entities.WarningStatusEntity;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

public interface IConsumerBatchMsgService {
    void saveMessageBatch(List<ConsumerRecord<?, ?>> records);

    void saveMessage(ConsumerRecord<?, ?> record);

    List<CoordinateEntity> getCoordinateEntityList();

    void setCoordinateEntityListEmpty();

    List<WarningStatusEntity> getWarningStatusEntityList();

    void setWarningStatusEntityListEmpty();

    List<TagWarningEntity> getTagWarningEntityList();

    void setTagWarningEntityListEmpty();
}
