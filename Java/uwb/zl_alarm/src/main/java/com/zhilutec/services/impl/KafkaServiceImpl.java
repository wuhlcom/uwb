package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zhilutec.services.IKafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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
            if (!alarms.isEmpty())
                alarmAll.addAll(alarms);
        }
        for (JSONObject jsonObject : alarmAll) {
            Integer op = jsonObject.getInteger("op");
            if (op.intValue() == 1) {
                alarmGen.add(jsonObject);
            }
        }
        return alarmGen;
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
