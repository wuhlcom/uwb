package com.zhilutec.uwb.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mia.common.constant.BaseConstant;
import com.mia.common.constant.ConfigConstants;
import com.mia.common.constant.EnvConstant;
import com.mia.common.constant.ServiceConstant;
import com.mia.common.dto.ResponseInfo;
import com.mia.common.enums.PeerModuleEnum;
import com.mia.common.enums.ServiceErrorEnum;
import com.mia.common.pojo.ZkRegisterInfo;
import com.mia.common.service.ZkServiceRegistry;
import com.mia.common.service.impl.ZkServiceRegistryImpl;
import com.mia.common.utils.HttpClient;
import com.zhilutec.uwb.kafka.producer.KafkaProducerListener;
import com.zhilutec.uwb.service.IKafkaService2;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KafkaServiceImpl2 implements IKafkaService2 {
	private static final Logger logger = LoggerFactory.getLogger(KafkaServiceImpl2.class);
    private KafkaTemplate<String, String> kafkaTemplate;
    private String registryServers;
    private String registryModule;
    private Integer registryNode;
    private String registryKafka;
    //是否需要去远端服务器获取zookeeper地址
    private Integer remote;
    private Integer confRemote;
    private String configuration;
    private ZkRegisterInfo regInfo;
    private ZkServiceRegistry serviceRegistry;
    
    public KafkaServiceImpl2(Integer remote, Integer confRemote, String registryServers, String registryModule, String registryKafka, Integer registryNode) {
    	this.remote = remote;
    	this.confRemote = confRemote;
    	this.registryServers = registryServers;
    	this.registryModule = registryModule;
    	this.registryNode = registryNode;
    	this.registryKafka = registryKafka;
    	this.serviceRegistry = new ZkServiceRegistryImpl();
    }
    
    private String sendRequestToDivider() {
    	String gateway = System.getenv(EnvConstant.DIV_GATEWAYADDR);
	    String port = System.getenv(EnvConstant.DIV_GATEWAYPORT);
	    String ns = System.getenv(EnvConstant.DIV_NAMESPACE);
	    String clus = System.getenv(EnvConstant.DIV_CLUSTER);
	    String module;
	    if(PeerModuleEnum.getByModule(registryModule).getConf()) {
	    	module = registryModule;
	    } else {
	    	module = ServiceConstant.NO_CONF_NODULE;
	    }
	    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://"+gateway+":"+port)
	            .queryParam(EnvConstant.DIV_POD_NAMESPACE, ns)
	            .queryParam(EnvConstant.DIV_POD_CLUSTER, clus)
	            .queryParam(EnvConstant.DIV_POD_MODULE, module);
	    return HttpClient.getWithUrlEncoded(builder);
    }
    
    private ZkRegisterInfo getServerAddr() {
    	if (BaseConstant.OFF == remote) {
    		logger.info("remote center off");
    		return new ZkRegisterInfo(registryServers, registryKafka, registryNode);
    	} else {
    		String request = PeerModuleEnum.getByModule(registryModule).toString();
    		if (null != request) {   			
    			String response = sendRequestToDivider();
    			logger.info("divider response"+response);
    			return JSON.parseObject(response, ZkRegisterInfo.class);
    		} else {
    			logger.info("no module named "+registryModule);
    			return null;
    		}
    	}    	
    }
    
    public KafkaTemplate<String, String> createKafkaTemplate(String bootstrapServers) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configProps));
    }
    
    public void readKafka(String data) {  
		JSONObject root = JSON.parseObject(data);      	
    	JSONObject kafkaInfo = root.getJSONObject(ServiceConstant.KAFKA_INFO);
    	JSONArray kafkaArray = kafkaInfo.getJSONArray(ServiceConstant.KAFKA);
		int replicationFactor = kafkaArray.size(); 	  	
		String kafkaBlockList = "";
		for (int i = 0; i < replicationFactor; i++) {
			if (i > 0) {
				kafkaBlockList += ",";
			}
			JSONObject tempData = (JSONObject)kafkaArray.get(i);
			kafkaBlockList += tempData.getString(ConfigConstants.SERVER);
			kafkaBlockList += ":";
			kafkaBlockList += tempData.getString(ConfigConstants.PORT);
		}
		regInfo.setKafka(kafkaBlockList);
		System.setProperty(ServiceConstant.CONSUMER_KAFKA, kafkaBlockList);
		logger.info("Get kafka server:" + kafkaBlockList);
    }
    
    @Override
    public ResponseInfo register(PeerModuleEnum own) {
    	regInfo = getServerAddr();
    	if (null != regInfo) {
    		while (false == serviceRegistry.connect(regInfo)) {
    			logger.error("Can not register service to cluser("+regInfo.getZk()+".");
    		}
    		ResponseInfo resp = serviceRegistry.register(own, confRemote);
    		logger.warn("zookeeper response "+resp.toString());
    		if (ServiceErrorEnum.SUCCESS.getCode() == resp.getErrcode()) {
    			configuration = resp.getMsg(); 
    			logger.warn("zookeeper configuration "+configuration);
    			if (false == configuration.isEmpty()) {
    				readKafka(configuration);
    			}   			
    		}
    		return resp;
    	} else {
    		logger.error("Failed to register.");
    		return new ResponseInfo(ServiceErrorEnum.SYSTEM_ERROR);
    	}		
	}
    
    @Override
    public ResponseInfo setProducer(List<PeerModuleEnum> neighborTypeList) {
    	//配置信息本身没有检查，回头再写
    	if (null == regInfo || null == regInfo.getKafka()) {
    		logger.error("No register information.");
    		return new ResponseInfo(ServiceErrorEnum.SYSTEM_ERROR);
    	}
    	serviceRegistry.initNeighbor(neighborTypeList);
		kafkaTemplate = createKafkaTemplate(regInfo.getKafka()); 
		return new ResponseInfo(ServiceErrorEnum.SUCCESS, configuration);
	}
    
    @Override
    public ResponseInfo setConsumer() {
    	logger.error("To be continue.");
		return new ResponseInfo(ServiceErrorEnum.SUCCESS, configuration);
	}

    @Override
    public ServiceErrorEnum send(String message, PeerModuleEnum neibor, String key) {
    	String topic = serviceRegistry.getNeighborTopic(neibor, key);
    	if (null == topic || topic.isEmpty()) {
    		logger.error("Can not get service node of "+neibor.getModule()+".");
    		return ServiceErrorEnum.SERVICE_OFFLINE;
    	} else {
    		logger.error("Send message to service "+neibor.getModule()+" on topic "+topic+".");
    		kafkaTemplate.send(topic, message);
    		return ServiceErrorEnum.SUCCESS;
    	}       
    }

    @Override
    public ServiceErrorEnum send(String message, String topic) {
    	if (null == topic || topic.isEmpty()) {
    		logger.error("Topic error.");
    		return ServiceErrorEnum.SERVICE_OFFLINE;
    	} else {
    		logger.error("Send message to topic "+topic+".");
			kafkaTemplate.setProducerListener(new KafkaProducerListener());
			kafkaTemplate.send(topic, message);
    		kafkaTemplate.send(topic, message);
    		return ServiceErrorEnum.SUCCESS;
    	}       
    }
}
