package com.zhilutec.services.impl;

import com.mia.common.constant.BaseConstant;
import com.mia.common.constant.ServiceConstant;
import com.mia.common.dto.DtoSend;
import com.mia.common.dto.ResponseInfo;
import com.mia.common.enums.PeerModuleEnum;
import com.mia.common.enums.ServiceErrorEnum;
import com.mia.common.service.impl.KafkaServiceImpl;
import com.zhilutec.kafka.producer.KafkaProducerProperty;
import com.zhilutec.services.ZkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

	private KafkaServiceImpl register;
	
	@Resource
    private KafkaProducerProperty kafkaProducerProperty;

	@Override
	public void afterPropertiesSet() throws Exception {
		PeerModuleEnum self = PeerModuleEnum.getByModule(registryModule);
		register = new KafkaServiceImpl(remote, confRemote, registryServers, registryModule, kafkaServers,
				registryNode);
		ResponseInfo resp = register.register(self);
		if (resp.getErrcode() != ServiceErrorEnum.SUCCESS.getCode()) {
			logger.error("Zookeeper initial failed.");
		}
		kafkaProducerProperty.setBootstrapServers(resp.getMsg());
		logger.error("kafkaProducerProperty BootstrapServers"+kafkaProducerProperty.getBootstrapServers()+".");
		String confData = resp.getMsg();
		reInitKafka(self, confData);
	}

	private void reInitKafka(PeerModuleEnum self, String data) {
		if (BaseConstant.ON == remote) {
			logger.info("get configuration " + data + ".");
			logger.info(" kafkaServers " + kafkaServers + ", registryNode " + registryNode);
		}
		System.setProperty(ServiceConstant.REGISTRY_RESULT_ENV, ServiceConstant.REGISTRY_RESULT_CONNECT);
	}
	
	public void init() throws Exception {		
		
	}

	@Override
	public ServiceErrorEnum sendToNeighbor(String data, PeerModuleEnum neighbor, String key) {
		return register.send(data, neighbor, key);
	}

	@Override
	public ServiceErrorEnum sendToNeighbor(String data, String topic) {
		return register.send(data, topic);
	}

	@Override
	public void push(List<DtoSend> pushes, PeerModuleEnum neighbor, String key) {
		try {
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
