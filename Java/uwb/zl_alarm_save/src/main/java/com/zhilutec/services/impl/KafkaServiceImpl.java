package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.TypeReference;

import com.zhilutec.dbs.entities.*;

import com.zhilutec.services.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;

import java.util.List;


@Service
public class KafkaServiceImpl implements IKafkaService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IWarningService warningService;

    private List<Warning> str2Warnings(List<ConsumerRecord<String, String>> records) {
        List<Warning> warnings = new ArrayList<>();
        List<Warning> warningAll = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            warnings = JSON.parseObject(record.value().toString(), new TypeReference<ArrayList<Warning>>() {
            });
            if (!warnings.isEmpty()) {
                warningAll.addAll(warnings);
            }
        }
        return warningAll;
    }

    @Override
    public void batchSave(List<ConsumerRecord<String, String>> records) {
        List<Warning> warnings = this.str2Warnings(records);
        if (warnings != null && warnings.size() > 0) {
            warningService.batchSave(warnings);
        }

    }


}
