package com.zhilutec.uwb.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zhilutec.uwb.service.IKafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class KafkaServiceImpl implements IKafkaService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    //获取报警消息,要剔除报警取消消息
    @Override
    public List<JSONObject> str2Json(List<ConsumerRecord<String, String>> records) {
        List<JSONObject> alarms = new ArrayList<>();
        List<JSONObject> alarmAll = new ArrayList<>();
        List<JSONObject> alarmGen = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            alarms = JSON.parseObject(record.value().toString(), new TypeReference<ArrayList<JSONObject>>() {
            });
            if (!alarms.isEmpty()) {
                alarmAll.addAll(alarms);
            }
        }
        for (JSONObject jsonObject : alarmAll) {
            Integer op = jsonObject.getInteger("op");
            if (op.intValue() == 1) {
                alarmGen.add(jsonObject);
            }
        }
        return alarmGen;
    }

    //获取报警消息,要剔除报警取消消息
    //获取坐标消息
    @Override
    public Map<String, List<JSONObject>> kafkaMsg(List<ConsumerRecord<String, String>> records) {
        Map<String, List<JSONObject>> msgMap = new HashMap<>();

        List<JSONObject> coordinateAll = new ArrayList<>();
        List<JSONObject> coordinateList = new ArrayList<>();

        List<JSONObject> warningList = new ArrayList<>();
        List<JSONObject> warningAll = new ArrayList<>();
        List<JSONObject> warningGenerate = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            Map<String, Object> map = JSONObject.parseObject(record.value().toString(), Map.class);
            if (map != null && !map.isEmpty()) {
                List<String> warnings = (List<String>) map.get("warnings");
                if (warnings != null && warnings.size() > 0) {
                    for (String warning : warnings) {
                        JSONObject jsonObject = JSONObject.parseObject(warning);
                        warningList.add(jsonObject);
                    }

                    if (!warningList.isEmpty()) {
                        warningAll.addAll(warningList);
                    }

                    // 剔除报警取消消息
                    for (JSONObject jsonObject : warningAll) {
                        Integer op = jsonObject.getInteger("op");
                        if (op.intValue() == 1) {
                            warningGenerate.add(jsonObject);
                        }
                    }
                }

                List<String> coordinates = (List<String>) map.get("coordinates");
                if (coordinates != null && coordinates.size() > 0) {
                    for (String coordinate : coordinates) {
                        JSONObject jsonObject = JSONObject.parseObject(coordinate);
                        coordinateList.add(jsonObject);
                    }

                    if (!coordinateList.isEmpty()) {
                        coordinateAll.addAll(coordinateList);
                    }
                }
            }
        }

        msgMap.put("warnings", warningGenerate);
        msgMap.put("coordinates", coordinateAll);
        return msgMap;
    }


    @Override
    public void wsSendMsgBase(Session wsSession, String rs) throws IOException {
        wsSession.getBasicRemote().sendText(rs);
    }

    @Override
    public void wsSendMsgAsync(Session wsSession, String rs) throws IOException {
        wsSession.getAsyncRemote().sendText(rs);
    }
}
