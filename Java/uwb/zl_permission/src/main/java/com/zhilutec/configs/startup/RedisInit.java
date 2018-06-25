package com.zhilutec.configs.startup;

import com.zhilutec.services.IRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Order(value = 1)
public class RedisInit implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IRedisService redisService;

    @Override
    public void run(String... args) throws Exception {
        logger.info(">>>>>>>>>>>>>>>服务启动执行:清空缓存数据<<<<<<<<<<<<");
        redisService.flushdb();
    }

}