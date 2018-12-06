package com.zhilutec.services.impl;

import com.zhilutec.services.IRedisService;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements IRedisService {

    @Resource
    protected RedisTemplate<String, Object> redisTemplate;

    @Resource
    protected HashOperations<String, String, Object> hashOperations;

    @Resource
    protected ZSetOperations<String, String> zSetOperations;

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
    public Object hashGet(String key, String field) {
        return hashOperations.get(key, field);
    }

    @Override
    public void hashAdd(String key, String field, Object obj, long expire) {
        hashOperations.put(key, field, obj);
        if (expire != -1) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
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

    @Override
    public List<Object> hashMGet(String key, Collection<String> collection) {
        List<Object> rs = (List<Object>) hashOperations.multiGet(key, collection);
        if (rs == null || rs.isEmpty()) {
            return null;
        }
        return rs;
    }

    @Override
    public Map hashGetMap(String keyPre, Object o) {
        String key = this.genRedisKey(keyPre, o);
        return hashGetMap(key);
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
     * 删除指定的hash value
     *
     * @param key   redis key
     * @param field 传入field的名称
     */
    @Override
    public Long hashDel(String key, String field) {
        return hashOperations.delete(key, field);
    }


    /**
     * 判断key是否存在redis中
     *
     * @param key   传入key的名称
     * @param field field
     * @return
     */
    @Override
    public boolean isHashKey(String key, String field) {
        return hashOperations.hasKey(key, field);
    }


    /**
     * 查询当前key下缓存数量
     *
     * @param key redis key
     * @return
     */
    @Override
    public long hashCount(String key) {
        return hashOperations.size(key);
    }


    //获取hash中所有的value值
    @Override
    public List<Object> hashValues(String key) {
        List<Object> objects = new ArrayList<>();
        List<Object> redisObjects = hashOperations.values(key);
        if (redisObjects == null) {
            return objects;
        }
        return redisObjects;
    }


    /**模糊匹配取出所有的hashkey*/
    @Override
    public Set<String> matchedKeys(String keyPre) {
        String keyPattern = keyPre + "*";
        return redisTemplate.keys(keyPattern);
    }

    @Override
    public Set<String> keys(String key) {
        String keyPattern = key;
        return redisTemplate.keys(keyPattern);
    }


    /**
     * 添加单个
     */
    @Override
    public Boolean zSetAdd(String keyPre, String member, Double score) {
        return zSetOperations.add(keyPre, member, score);
    }


    /**
     * 批量添加
     **/
    @Override
    public Boolean zSetAdd(String keyPre, Set<String> members) {
        double score = (double) (System.currentTimeMillis() / 1000);
        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        for (String member : members) {
            ZSetOperations.TypedTuple<String> tuple = new DefaultTypedTuple<String>(member, score);
            tuples.add(tuple);
        }
        Long num = zSetOperations.add(keyPre, tuples);
        if (num > 0) {
            return true;
        }
        return false;
    }


    /**
     * 根据members删除
     */
    @Override
    public void zSetDel(String keyPre, Set<String> members) {
        zSetOperations.remove(keyPre, members);
    }

    /**
     * 按下标查询指定下标范围内的值,下标是从0开始
     */
    @Override
    public Set<String> zSetRange(String keyPre, Long min, Long max) {
        return zSetOperations.range(keyPre, min, max);
    }

    /**
     * 根据Scorce查询,从小到大，在指定的scorce范围内，查询指定下标开始的记录数
     * NEGATIVE_INFINITY  负无穷大
     * POSITIVE_INFINITY  正无穷大
     */
    @Override
    public Set<String> zSetRangeByScore(String keyPre, double min, double max, long offset, long limit) {
        return zSetOperations.rangeByScore(keyPre, min, max, offset, limit);
    }


    /**
     * 根据Score查询,从大到小，在指定的scorce范围内，查询指定下标开始的记录数
     * NEGATIVE_INFINITY  负无穷大
     * POSITIVE_INFINITY  正无穷大
     */
    @Override
    public Set<String> zSetReverseRangeByScore(String keyPre, double min, double max, long offset, long limit) {
        return zSetOperations.reverseRangeByScore(keyPre, min, max, offset, limit);
    }

    /**
     * 根据Score从小到大排序，取出其下标
     */
    @Override
    public Long zSetRank(String keypre, String member) {
        return zSetOperations.rank(keypre, member);
    }

    /**
     * 根据Score从大到小排序，取出其下标
     */
    @Override
    public Long zSetReverseRank(String keypre, String member) {
        return zSetOperations.reverseRank(keypre, member);
    }

    /**
     * 根据score范围，统计member数量
     */
    @Override
    public Long zSetCount(String keypre, Double min, Double max) {
        return zSetOperations.count(keypre, min, max);
    }

    /**
     * 统计member数量
     */
    @Override
    public Long zSetCard(String keypre) {
        return zSetOperations.zCard(keypre);
    }


}