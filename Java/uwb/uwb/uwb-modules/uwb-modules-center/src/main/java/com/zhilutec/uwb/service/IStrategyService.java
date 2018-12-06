package com.zhilutec.uwb.service;

import com.zhilutec.uwb.common.pojo.RedisPolicy;

import java.util.List;

public interface IStrategyService {

    List<RedisPolicy> getRedisPolicies(String key);

    List<RedisPolicy> getRedisPolicies(Long tagId);

}
