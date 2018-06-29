package com.zhilutec.controllers;

import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.kafka.KafkaProducerProperty;
import com.zhilutec.kafka.KafkaProducerListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/kafka")
public class KafkaController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    KafkaProducerProperty kafkaConfig;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public String sendKafka(HttpServletRequest request, HttpServletResponse response) {
        try {
            String message = request.getParameter("message");
            logger.info("kafka的消息={}", message);
            kafkaTemplate.send(kafkaConfig.getTopic(), "key", message);
            kafkaTemplate.setProducerListener(new KafkaProducerListener());

            logger.info("发送kafka成功.");
            return Result.ok(ResultCode.SUCCESS.getCode(), "发送kafka成功").toJSONString();
        } catch (Exception e) {
            logger.error("发送kafka失败", e);
            return Result.error("发送kafka失败").toJSONString();
        }
    }

}
