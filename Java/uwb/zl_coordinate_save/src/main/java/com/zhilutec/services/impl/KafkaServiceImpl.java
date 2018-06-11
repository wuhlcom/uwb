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


/**坐标消息入库*/
@Service
public class KafkaServiceImpl implements IKafkaService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    ICoordinateService coordinateService;


    public List<Coordinate> str2Coordinate(List<ConsumerRecord<String, String>> records) {
        List<Coordinate> coordinates = new ArrayList<>();
        List<Coordinate> coordinateAll = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            coordinates = JSON.parseObject(record.value().toString(), new TypeReference<ArrayList<Coordinate>>(){});
            if (!coordinates.isEmpty()){
                coordinateAll.addAll(coordinates);
            }
        }
        return coordinateAll;
    }


    @Override
    public void batchSave(List<ConsumerRecord<String, String>> records) {
        List<Coordinate> coordinates = this.str2Coordinate(records);
        coordinateService.batchSave(coordinates);
    }




}
