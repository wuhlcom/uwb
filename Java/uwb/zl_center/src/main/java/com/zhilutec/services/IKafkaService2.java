package com.zhilutec.services;

import java.util.List;

import com.mia.common.enums.PeerModuleEnum;
import com.mia.common.dto.ResponseInfo;
import com.mia.common.enums.ServiceErrorEnum;

public interface IKafkaService2 {
	public ResponseInfo register(PeerModuleEnum own);
	
	public ResponseInfo setProducer(List<PeerModuleEnum> neighborTypeList);
	
	public ResponseInfo setConsumer();
	
    public ServiceErrorEnum send(String message, PeerModuleEnum neibor, String key);
    
    public ServiceErrorEnum send(String message, String topic);
}
