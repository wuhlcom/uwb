package com.zhilutec.services;

import java.io.IOException;
import java.util.concurrent.Future;

import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.db.entities.WarningStatusEntity;

public interface ITcpHandlerService {


	String convert(String receiveMsg);


	void saveMessage(String receiveMsg);

	void saveStatus(String jsonStr);


	boolean isAbsence(String code, Long prisonerTagId);


	void sendMultiAreaMsg(Long tcpTagId, Session wsSession, JSONObject wsMsgObj, WarningStatusEntity data);


	String getPrisonerMsg(JSONObject jsonObject);

	void saveMessage(Long tagId);


    Future<Void> wsSendMsg(Session wsSession, String rs);

    void sendPrisonerMsg(Long tcpTagId, Session wsSession, JSONObject wsMsgObj, WarningStatusEntity data,
                         JSONObject tcpMsgJson) throws Exception;

	void sendMultiAreaMsgNew(Session wsSession, JSONObject wsMsgObj, WarningStatusEntity tcpData) throws IOException, ClassNotFoundException;

	void sendAreaMsg(Long tcpTagId, Session wsSession, JSONObject wsMsgObj) throws Exception;

	String getStatisticsInfo(JSONObject wsMsgObj) throws Exception;

	String getWaningAmount();
	String resetWarning(JSONObject jsonParam);
}
