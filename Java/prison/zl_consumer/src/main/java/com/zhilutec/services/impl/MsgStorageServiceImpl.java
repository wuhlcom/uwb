package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.db.entities.CoordinateEntity;
import com.zhilutec.db.entities.TagWarningEntity;
import com.zhilutec.db.entities.WarningStatusEntity;
import com.zhilutec.services.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据入库
 */
@Service
public class MsgStorageServiceImpl implements IMsgStorageService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IConsumerOneService consumerOneService;

    @Resource
    IConsumerBatchMsgService consumerBatchMsgService;

    @Resource
    ICoordinateService coordinateService;

    @Resource
    IWarningStatusService warningStatusService;

    @Resource
    ITagWarningService tagWarningService;

    /**
     * 逐条数据保存
     */
    @Override
    public void saveData(ConsumerRecord<?, ?> record) {
        try {
            Long timeMillis = System.currentTimeMillis();

            // 将处理过后的消息保存到map和TagCoordinate,TagWarningStatus表
            consumerOneService.saveMessage(record);

            //将消息转为json对象
            JSONObject kafkaJson = JSON.parseObject(record.value().toString());
            Long tcpTagId = kafkaJson.getLong("tag_id");
            if (tcpTagId == null) {
                logger.info("-------------------kafka消息中不找不到tagId--------------------");
            }
            // 保存报警到TagWarning表
            consumerOneService.saveMessage(tcpTagId);

            Long timeMillis2 = System.currentTimeMillis();
            // Long value = (timeMillis2 - timeMillis);
            // logger.warn("kafka consumer 收到消息时间:" + timeMillis.toString());
            // logger.warn("kafka consumer 保存消息完成时间:" + timeMillis2.toString());
            // logger.warn("kafka consumer 保存消息用时:" + value.toString() + "ms");
        } catch (Exception e) {
            e.printStackTrace();
            // logger.info("Kafka Consumer-Mysql 保存消息错误:" + e.getMessage());
        }
    }

    @Override
    public void saveDataBatch(List<ConsumerRecord<?, ?>> records) {
        try {
            consumerBatchMsgService.saveMessageBatch(records);
            // List<TagWarningEntity> tagStatusEntities = consumerBatchMsgService.getTagWarningEntityList();
            List<WarningStatusEntity> warningStatusEntities = consumerBatchMsgService.getWarningStatusEntityList();
            List<CoordinateEntity> coordinateEntities = consumerBatchMsgService.getCoordinateEntityList();

            // this.saveTagWarningEntities(tagStatusEntities);
            if (warningStatusEntities != null && warningStatusEntities.size() >= 1) {
                this.saveWarningStatusEntities(warningStatusEntities);
            }
            if (coordinateEntities != null && coordinateEntities.size() >= 1) {
                this.saveCoordinateEntities(coordinateEntities);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveWarningStatusEntities(List<WarningStatusEntity> warningStatusEntities) {
        try {
            warningStatusService.saveBatch(warningStatusEntities);
            consumerBatchMsgService.setWarningStatusEntityListEmpty();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveCoordinateEntities(List<CoordinateEntity> coordinateEntities) {
        try {
            coordinateService.saveBatch(coordinateEntities);
            consumerBatchMsgService.setCoordinateEntityListEmpty();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveTagWarningEntities(List<TagWarningEntity> tagWarningEntities) {
        try {
            tagWarningService.saveBatch(tagWarningEntities);
            consumerBatchMsgService.setWarningStatusEntityListEmpty();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
