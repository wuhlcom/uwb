package com.zhilutec.services.impl;


import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.dbs.daos.StrategyDao;
import com.zhilutec.dbs.pojos.RedisPolicy;
import com.zhilutec.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class RedisPolicyServiceImpl extends IRedisService<RedisPolicy> implements IRedisPolicyService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    StrategyDao strategyDao;

    @Resource
    IPersonService personService;

    @Resource
    IFenceService fenceService;

    @Resource
    IDepartmentService departmentService;

    @Override
    public List<RedisPolicy> getRedisPolicies(String key) {
        return this.getAll(key);
    }

    @Override
    public List<RedisPolicy> getRedisPolicies(Long tagId) {
        String key = ConstantUtil.POLICY_KEY_PRE + ":" + Long.toString(tagId);
        return this.getAll(key);
    }

    /**
     * @param key   tagId
     * @param field strategyCode
     */
    @Override
    public RedisPolicy getReidsPolicy(String key, String field) {
        return this.get(key, field);
    }

    /**
     * @param key   tagId
     * @param field strategyCode
     */
    @Override
    public void deleteRedisPolicy(String key, String field) {
        this.remove(key, field);
    }

    /**
     * @param key   tagId
     * @param field strategyCode
     */
    @Override
    public void putRedisPolicy(String key, String field, RedisPolicy strategy, long expire) {
        this.put(key, field, strategy, expire);
    }

    @Override
    protected String getRedisKey() {
        return null;
    }

}
