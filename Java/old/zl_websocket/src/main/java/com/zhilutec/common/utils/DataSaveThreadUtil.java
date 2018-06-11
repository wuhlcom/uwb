package com.zhilutec.common.utils;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.services.ICoordinateRedisService;
import com.zhilutec.services.ITagStatusRedisService;
import com.zhilutec.services.ITagWarningRedisService;

public class DataSaveThreadUtil implements Runnable {

	@Autowired
	private ICoordinateRedisService coorRedisService;

	@Autowired
	private ITagWarningRedisService wornigRedisService;

	@Autowired
	private ITagStatusRedisService statusService;

	private String thrName;
	private String receiveMsg;


	public DataSaveThreadUtil() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DataSaveThreadUtil(String receiveMsg) {
		super();
		this.receiveMsg = receiveMsg;
	}
	
	
	public DataSaveThreadUtil(String thrName, String receiveMsg) {
		super();
		this.thrName = thrName;
		this.receiveMsg = receiveMsg;
	}


	@Override
	public void run() {
		saveMessage(this.receiveMsg);

	}

	// 保存消息到数据库
	public void saveMessage(String receiveMsg) {
		JSONObject tcpMsgObj = JSON.parseObject(receiveMsg);
		// 保存状态到redis,并保存消息到数据库
		Integer type = tcpMsgObj.getInteger("type");
		coorRedisService.saveCoordinate(receiveMsg);

		// type 1在离线消息 type 3报警消息
		if (type.intValue() == 1) {
			statusService.saveStatus(receiveMsg);
		} else if (type.intValue() == 3) {
			wornigRedisService.saveWarning(receiveMsg);
		}
	}


	public String getThrName() {
		return thrName;
	}



	public void setThrName(String thrName) {
		this.thrName = thrName;
	}



	public String getReceiveMsg() {
		return receiveMsg;
	}



	public void setReceiveMsg(String receiveMsg) {
		this.receiveMsg = receiveMsg;
	}

	
}
