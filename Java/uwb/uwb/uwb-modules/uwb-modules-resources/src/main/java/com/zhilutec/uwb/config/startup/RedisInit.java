package com.zhilutec.uwb.config.startup;


import com.zhilutec.uwb.service.IRedisService;
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