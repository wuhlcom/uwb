package com.zhilutec.uwb.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.zhilutec.uwb.service.*;
import com.zhilutec.uwb.entity.Person;
import com.zhilutec.uwb.entity.Status;
import com.zhilutec.uwb.util.ConstantUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 消息入库
 */
@Service
public class KafkaServiceImpl implements IKafkaService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IStatusService statusService;

    @Resource
    IPersonService personService;


    public List<Status> handleStatus(List<ConsumerRecord<String, String>> records) {
        List<Status> statuses = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            String kafkaMsg = record.value().toString();
            JSONObject msgObj = JSON.parseObject(kafkaMsg);
            Integer type = msgObj.getInteger("type");
            //只处理type 7 消息
            if (type == ConstantUtil.ENGINE_STATUS) {
                Status status = JSONObject.parseObject(msgObj.toJSONString(), Status.class);
                Person person = personService.getCache(status.getTagId());
                if (person != null) {
                    //处理状态消息
                    if (status != null) {
                        status.setPersonName(person.getPersonName());
                        statuses.add(status);
                    }
                }else {
                    logger.info("person tagId not found!");
                }
            }
        }
        return statuses;
    }

    @Override
    public void batchSave(List<ConsumerRecord<String, String>> records) {
        List<Status> statuses = this.handleStatus(records);
        statusService.batchSave(statuses);
    }


}
