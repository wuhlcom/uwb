package com.zhilutec.services;

import com.mia.common.dto.DtoSend;
import com.mia.common.enums.PeerModuleEnum;
import com.mia.common.enums.ServiceErrorEnum;

import java.util.List;


public interface ZkService {	
	public ServiceErrorEnum sendToNeighbor(String data, PeerModuleEnum neighbor, String key);

	public void push(List<DtoSend> pushes, PeerModuleEnum neighbor, String key);
}

