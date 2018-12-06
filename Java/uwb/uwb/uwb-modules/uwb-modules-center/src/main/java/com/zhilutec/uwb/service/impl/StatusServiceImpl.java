package com.zhilutec.uwb.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.uwb.entity.Status;
import com.zhilutec.uwb.service.IRedisService;
import com.zhilutec.uwb.service.IStatusService;
import com.zhilutec.uwb.util.ConstantUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;


@Service
public class StatusServiceImpl implements IStatusService {

    @Resource
    IRedisService redisService;


    @Override
    public void addCache(String keyPre, Long tagId, String field, Object o) {
        String key = redisService.genRedisKey(keyPre, tagId);
        this.addCache(key, field, o);
    }


    @Override
    public void addCache(String key, String field, Object o) {
        redisService.hashAdd(key, field, o, ConstantUtil.REDIS_DEFAULT_TTL);
    }

    @Override
    public void addCache(Long tagId, Status status) {
        String keyPre = ConstantUtil.STATUS_KEY_PRE;
        String key = redisService.genRedisKey(keyPre, tagId);

        String str = JSON.toJSONString(status);
        Map map = JSONObject.parseObject(str, Map.class);
        redisService.hashAddMap(key, map, ConstantUtil.REDIS_DEFAULT_TTL);
    }


    @Override
    public Status getCache(Long tagId) {
        Status status = null;

        String key = redisService.genRedisKey(ConstantUtil.STATUS_KEY_PRE, tagId);
        Map map = redisService.hashGetMap(key);


        if (map == null) {
            return status;
        }
        String mapStr = JSONObject.toJSONString(map);

        status = JSONObject.parseObject(mapStr, Status.class);
        return status;
    }

    @Override
    public Object getCache(String key, String field) {
        return redisService.hashGet(key, field);
    }


}
