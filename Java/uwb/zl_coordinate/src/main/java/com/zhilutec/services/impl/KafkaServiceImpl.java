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

    @Override
    public List<JSONObject> str2Json(List<ConsumerRecord<String, String>> records) {
        List<JSONObject> coordinates = new ArrayList<>();
        List<JSONObject> coordinateAll = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            coordinates = JSON.parseObject(record.value().toString(), new TypeReference<ArrayList<JSONObject>>() {
            });
            if (!coordinates.isEmpty())
                coordinateAll.addAll(coordinates);
        }
        return coordinateAll;
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
