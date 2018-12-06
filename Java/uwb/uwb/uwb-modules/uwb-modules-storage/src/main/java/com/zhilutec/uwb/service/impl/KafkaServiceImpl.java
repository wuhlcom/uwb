package com.zhilutec.uwb.service.impl;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;


import com.alibaba.fastjson.TypeReference;
import com.zhilutec.uwb.service.*;
import com.zhilutec.uwb.entity.Coordinate;
import com.zhilutec.uwb.entity.Warning;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class KafkaServiceImpl implements IKafkaService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IWarningService warningService;

    @Resource
    ICoordinateService coordinateService;

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

    private List<Coordinate> str2Coordinate(List<ConsumerRecord<String, String>> records) {
        List<Coordinate> coordinates = new ArrayList<>();
        List<Coordinate> coordinateAll = new ArrayList<>();

        for (ConsumerRecord<String, String> record : records) {
            coordinates = JSON.parseObject(record.value().toString(), new TypeReference<ArrayList<Coordinate>>() {
            });
            if (!coordinates.isEmpty()) {
                coordinateAll.addAll(coordinates);
            }
        }
        return coordinateAll;
    }

    //数据入库
    @Override
    public void storage(List<ConsumerRecord<String, String>> records) {
        Map<String, List<Object>> msgMap = new HashMap<>();

        List<Warning> warningList = new ArrayList<>();
        List<Warning> warningAll = new ArrayList<>();
        List<Warning> warningGenerate = new ArrayList<>();

        List<Coordinate> coordinateAll = new ArrayList<>();
        List<Coordinate> coordinateList = new ArrayList<>();

        for (ConsumerRecord<String, String> record : records) {
            Map<String, Object> map = JSONObject.parseObject(record.value().toString(), Map.class);
            if (map != null && !map.isEmpty()) {
                List<String> warnings = (List<String>) map.get("warnings");
                if (warnings != null && warnings.size() > 0) {
                    for (String warning : warnings) {
                        Warning warningObj=JSONObject.parseObject(warning,Warning.class);
                        warningList.add(warningObj);
                    }

                    if (!warningList.isEmpty()) {
                        warningAll.addAll(warningList);
                    }

                    // 剔除报警取消消息
                    for (Warning warning : warningAll) {
                        Integer op = warning.getOp();
                        if (op.intValue() == 1) {
                            warningGenerate.add(warning);
                        }
                    }
                }

                List<String> coordinates = (List<String>) map.get("coordinates");
                if (coordinates != null && coordinates.size() > 0) {
                    for (String coordinate : coordinates) {
                        Coordinate coordinateObj = JSONObject.parseObject(coordinate,Coordinate.class);
                        coordinateList.add(coordinateObj);
                    }

                    if (!coordinateList.isEmpty()) {
                        coordinateAll.addAll(coordinateList);
                    }
                }

                if (warningAll != null && warningAll.size() > 0) {
                    warningService.batchSave(warningAll);
                }


                if (coordinateAll != null && coordinateAll.size() > 0) {
                    coordinateService.batchSave(coordinateAll);
                }
            }
        }

    }

}
