package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.db.entities.WarningStatusEntity;

import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.Future;

public interface IWebsocketService {

    void sendMultiAreaMsg(Session wsSession, JSONObject wsMsgObj, WarningStatusEntity tcpData);

    void sendAreaMsg(Long tcpTagId, Session wsSession, JSONObject wsMsgObj) throws Exception;

    void sendPrisonerMsg(Long tcpTagId, Session wsSession, JSONObject wsMsgObj, WarningStatusEntity tcpData,
                         JSONObject tcpMsgJson) throws Exception;

    Future<Void> wsSendMsg(Session wsSession, String rs);

    void  wsSendMsgBase(Session wsSession, String rs) throws IOException;

    String getPrisonerMsg(JSONObject jsonObject);

    void saveMessage(String receiveMsg);

    String convertZero(String receiveMsg);

    boolean isAbsence(String code, Long prisonerTagId);

    void saveStatus(String jsonStr);

    String getStatisticsInfo(JSONObject wsMsgObj) throws Exception;

    String getWaningAmount();

    String resetWarning(JSONObject jsonParam);
}
