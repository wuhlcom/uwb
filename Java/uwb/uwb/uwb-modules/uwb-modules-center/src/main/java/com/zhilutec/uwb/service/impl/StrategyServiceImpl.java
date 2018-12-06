package com.zhilutec.uwb.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.zhilutec.uwb.common.pojo.RedisPolicy;
import com.zhilutec.uwb.service.IRedisService;
import com.zhilutec.uwb.service.IStrategyService;
import com.zhilutec.uwb.util.ConstantUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StrategyServiceImpl implements IStrategyService {

    @Resource
    IRedisService redisService;


    @Override
    public List<RedisPolicy> getRedisPolicies(String key) {
        List<RedisPolicy> redisPolicies = new ArrayList<>();
        List<Object> objects = redisService.hashValues(key);
        if (objects != null) {
            for (Object object : objects) {
                String jsonStr = JSONObject.toJSONString(object);
                RedisPolicy redisPolicy = JSONObject.parseObject(jsonStr, RedisPolicy.class);
                redisPolicies.add(redisPolicy);
            }
        }
        return redisPolicies;
    }

    @Override
    public List<RedisPolicy> getRedisPolicies(Long tagId) {
        String key = ConstantUtil.POLICY_KEY_PRE + ":" + Long.toString(tagId);
        return this.getRedisPolicies(key);
    }

}
