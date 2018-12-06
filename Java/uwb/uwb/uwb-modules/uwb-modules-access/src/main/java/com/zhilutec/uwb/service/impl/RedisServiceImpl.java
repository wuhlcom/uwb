package com.zhilutec.uwb.service.impl;


import com.zhilutec.uwb.service.IRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.zhilutec.uwb.util.ConstantUtil;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements IRedisService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    protected RedisTemplate<String, Object> redisTemplate;

    @Resource
    protected HashOperations<String, String, Object> hashOperations;


    /**
     * 获取redis连接
     */
    private RedisConnection getConn() {
        RedisConnectionFactory redisConnectionFactory = redisTemplate.getConnectionFactory();
        RedisConnection redisConnection = redisConnectionFactory.getConnection();
        return redisConnection;
    }

    /**
     * 清空当前表
     */
    @Override
    public void flushdb() {
        this.getConn().flushDb();
    }

    /**
     * 生成key
     */
    @Override
    public String genRedisKey(String keyPre, Object o) {
        return keyPre + ":" + o.toString();
    }


    @Override
    public void hashAddMap(String key, Map map, Long expire) {
        hashOperations.putAll(key, map);
        if (expire != -1) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    @Override
    public Map hashGetMap(String key) {
        Map rs = hashOperations.entries(key);
        if (rs == null || rs.isEmpty()) {
            return null;
        }
        return rs;
    }

    /**
     * 删除
     *
     * @param key redis key,string,hash,list,set zset都可以删除
     */
    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void delete(String keyPre, Object o) {
        String key = this.genRedisKey(keyPre, o);
        delete(key);
    }

    /**
     * 批量删除
     ***/
    @Override
    public void delete(Collection<String> collection) {
        redisTemplate.delete(collection);
    }


    /**
     * 删除指定的hash value
     *
     * @param key   redis key
     * @param field 传入field的名称
     */
    @Override
    public Long hashDel(String key, String field) {
        return hashOperations.delete(key, field);
    }

    @Override
    public Set<String> keys(String keyPre) {
        String keyPattern = keyPre + "*";
        return redisTemplate.keys(keyPattern);
    }

    @Override
    public void delByKeys(String keyPattern) {
        Set<String> set = keys(keyPattern);
        if (set != null && !set.isEmpty()) {
            delete(set);
        }
    }

    //删除所有reidis缓存
    @Override
    public void delAll() {
        List<String> redisKeys = ConstantUtil.ACCESS_REDIS_KEYS;
        for (String redisKey : redisKeys) {
            logger.info("=========清理Redis Key==============:{}", redisKey);
            delByKeys(redisKey);
        }
    }


}