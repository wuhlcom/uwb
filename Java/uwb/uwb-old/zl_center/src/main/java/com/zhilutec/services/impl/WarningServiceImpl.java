package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.dbs.entities.Warning;
import com.zhilutec.services.IRedisService;
import com.zhilutec.services.IWarningService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class WarningServiceImpl implements IWarningService {

    @Resource
    IRedisService redisService;


    //获取缓存中围栏报警
    @Override
    public List<Warning> getFenceWarnings(String key) {
        List<Object> objects = redisService.hashValues(key);
        List<Warning> warnings = new ArrayList<>();
        for (Object object : objects) {
            Warning warning = null;
            String mapStr = JSONObject.toJSONString(object);
            warning = JSONObject.parseObject(mapStr, Warning.class);
            warnings.add(warning);
        }
        return warnings;
    }

    //获取缓存中其它报警
    @Override
    public Warning getCache(String key) {
        Warning warning = null;
        Map map = redisService.hashGetMap(key);
        if (map == null || map.isEmpty()) {
            return warning;
        }
        String mapStr = JSONObject.toJSONString(map);

        warning = JSONObject.parseObject(mapStr, Warning.class);
        return warning;
    }

    @Override
    public Warning getCache(String keyPre, Object o) {
        String key = redisService.genRedisKey(keyPre, o);
        return getCache(key);
    }

    @Override
    public List<Warning> getFenceWarnings(Long tagId) {
        String key = redisService.genRedisKey(ConstantUtil.FENCE_ALARM_KEY_PRE, tagId);
        List<Object> objects = redisService.hashValues(key);
        List<Warning> warnings = new ArrayList<>();
        for (Object object : objects) {
            Warning warning = null;
            String mapStr = JSONObject.toJSONString(object);
            warning = JSONObject.parseObject(mapStr, Warning.class);
            warnings.add(warning);
        }
        return warnings;
    }


    //添加围栏报警
    @Override
    public void addRedisWarning(Long tagId, String strategyCode, Warning warning) {
        String warningKey = redisService.genRedisKey(ConstantUtil.FENCE_ALARM_KEY_PRE, tagId);
        redisService.hashAdd(warningKey, strategyCode, warning, ConstantUtil.REDIS_DEFAULT_TTL);
    }


    //删除单个报警缓存
    @Override
    public Long delRedisWarning(String strategyCode, Long tagId, String warningKeyPre) {
        String key = redisService.genRedisKey(warningKeyPre, tagId);
        return redisService.hashDel(key, strategyCode);
    }

    //添加其它报警
    @Override
    public void addCache(String keyPre, Long tagId, Warning warning) {
        String key = redisService.genRedisKey(keyPre, tagId);

        String str = JSON.toJSONString(warning);
        Map map = JSONObject.parseObject(str, Map.class);
        redisService.hashAddMap(key, map, ConstantUtil.REDIS_DEFAULT_TTL);
    }

}
