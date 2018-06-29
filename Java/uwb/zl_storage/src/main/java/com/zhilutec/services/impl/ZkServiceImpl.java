package com.zhilutec.services.impl;

import com.mia.common.constant.BaseConstant;
import com.mia.common.constant.ServiceConstant;
import com.mia.common.dto.DtoSend;
import com.mia.common.dto.ResponseInfo;
import com.mia.common.enums.PeerModuleEnum;
import com.mia.common.enums.ServiceErrorEnum;
import com.zhilutec.services.ZkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@EnableKafka
@Service
public class ZkServiceImpl implements InitializingBean,ZkService {
	private static final Logger logger = LoggerFactory.getLogger(ZkServiceImpl.class);
	@Value("${env.registry.kafka}")
    private String kafkaServers;
    @Value("${env.registry.servers}")
    private String registryServers;
    @Value("${env.registry.module}")
    private String registryModule;    
    @Value("${env.registry.node}")
    private Integer registryNode;
    //是否需要去远端服务器获取zookeeper地址
    @Value("${env.registry.remote}")
    private Integer remote;  
    @Value("${env.registry.conf.remote}")
    private Integer confRemote;
	private KafkaServiceImpl2 kafkaService2;

	@Override
	public void afterPropertiesSet() throws Exception {
		PeerModuleEnum self = PeerModuleEnum.getByModule(registryModule);
		kafkaService2 = new KafkaServiceImpl2(remote, confRemote, registryServers, registryModule, kafkaServers,
				registryNode);
		ResponseInfo resp = kafkaService2.register(self);
		if (resp.getErrcode() != ServiceErrorEnum.SUCCESS.getCode()) {
			logger.error("Zookeeper initial failed.");
		}
		String confData = resp.getMsg();
		List<PeerModuleEnum> neighbor = new ArrayList<PeerModuleEnum>();
		neighbor.add(PeerModuleEnum.ACTION);
		resp = kafkaService2.setProducer(neighbor);
		reInitKafka(self, confData);
	}

	private void reInitKafka(PeerModuleEnum self, String data) {
		if (BaseConstant.ON == remote) {
			logger.info("get configuration " + data + ".");
			logger.info(" kafkaServers " + kafkaServers + ", registryNode " + registryNode);
		}
		String topic = MessageFormat.format(self.getName(), String.valueOf(registryNode));
		logger.info("topic is " + topic + ".");
		// System.setProperty(ServiceConstant.CONSUMER_TOPIC, topic);
		System.setProperty(ServiceConstant.REGISTRY_RESULT_ENV, ServiceConstant.REGISTRY_RESULT_CONNECT);
	}
	
	public void init() throws Exception {		
		
	}

	@Override
	public ServiceErrorEnum sendToNeighbor(String data, PeerModuleEnum neighbor, String key) {
		return kafkaService2.send(data, neighbor, key);
	}

	@Override
	public void push(List<DtoSend> pushes, PeerModuleEnum neighbor, String key) {
		try {
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
