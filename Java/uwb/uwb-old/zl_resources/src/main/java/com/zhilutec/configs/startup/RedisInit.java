package com.zhilutec.configs.startup;

import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Order(value = 1)
public class RedisInit implements CommandLineRunner {
    @Resource
    IRedisService redisService;

    @Override
    public void run(String... args) throws Exception {
        redisService.initRedis();
    }

}