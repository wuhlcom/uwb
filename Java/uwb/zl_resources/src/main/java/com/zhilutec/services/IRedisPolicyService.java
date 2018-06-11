package com.zhilutec.services;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.dbs.entities.Strategy;
import com.zhilutec.dbs.pojos.RedisPolicy;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

public interface IRedisPolicyService {
    List<RedisPolicy> getRedisPolicies(String key);

    List<RedisPolicy> getRedisPolicies(Long tagId);

    RedisPolicy getReidsPolicy(String key, String field);

    void  deleteRedisPolicy(String key, String field);

    void  putRedisPolicy(String key, String field, RedisPolicy strategy, long expire);
}
