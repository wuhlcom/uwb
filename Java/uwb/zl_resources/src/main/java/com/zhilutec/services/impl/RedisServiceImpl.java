package com.zhilutec.services.impl;

import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

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

    @Resource
    protected ListOperations<String, Object> listOperations;

    @Resource
    protected ValueOperations<String, String> valueOperations;


    @Resource
    IPersonService personService;

    @Resource
    IStrategyService strategyService;

    @Resource
    IFenceService fenceService;

    @Resource
    IDepartmentService departmentService;

    @Resource
    ILevelService levelService;

    @Resource
    IPositionService positionService;


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

    /**
     * 删除
     *
     * @param key redis key,string,hash,list,set zset都可以删除
     */
    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 批量删除
     ***/
    @Override
    public void delete(Collection<String> collection) {
        redisTemplate.delete(collection);
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

    /**
     * 清空指定hash key的所有field对应的value
     */
    @Override
    public void hashEmpty(String key) {
        //set保存field的集合
        Set<String> set = hashOperations.keys(key);
        set.stream().forEach(field -> hashOperations.delete(key, field));
    }


    //根据获取单个list中的第一个对象
    @Override
    public Object firstList(String key) {
        return this.getList(key, 0);
    }

    //根据获取单个list中某个对象
    public Object getList(String key, long index) {
        return listOperations.index(key, index);
    }

    //查询所有
    public List<Object> listAll(String key) {
        return this.getSomeList(key, 0, -1);
    }

    //获取某个范围内的对象
    public List<Object> getSomeList(String key, long start, long end) {
        return listOperations.range(key, start, end);
    }

    //在list头添加单个对象
    @Override
    public void lelfPush(String key, Object obj, long expire) {
        listOperations.leftPush(key, obj);
        if (expire != -1) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    //在list头添加多个对象
    @Override
    public void lelfPush(String key, List<?> objLs, long expire) {
        listOperations.leftPushAll(key, objLs);
        if (expire != -1) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    //删除list中某个对象
    public Long listDel(String key, long count, Object obj) {
        return listOperations.remove(key, count, obj);
    }


    @Override
    public void add(String key, String value, Long expireTime, TimeUnit timeUnit) {
        valueOperations.set(key, value, expireTime, timeUnit);
    }


    @Override
    public String get(String key) {
        return (String) valueOperations.get(key);
    }


    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
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

    @Override
    public void delAll() {
        List<String> redisKeys = ConstantUtil.REDIS_KEYS;
        for (String redisKey : redisKeys) {
            logger.info("=========清理Redis Key==============:{}", redisKey);
            delByKeys(redisKey);
        }
    }


    @Override
    public void initRedis() throws InterruptedException {
        logger.info(">>>>>>>>>>>>>>>服务启动执行:清空缓存数据<<<<<<<<<<<<");
        // 清空整个db
        // this.flushdb();
        // 删除db中指定key
        this.delAll();

        logger.info(">>>>>>>>>>>>>>>服务启动执行:初始化人员缓存数据<<<<<<<<<<<<");
        personService.personCacheInit();

        logger.info(">>>>>>>>>>>>>>>服务启动执行:初始化策略缓存数据<<<<<<<<<<<<");
        strategyService.policyCacheInit();

        logger.info(">>>>>>>>>>>>>>>服务启动执行:初始化围栏缓存数据<<<<<<<<<<<<");
        fenceService.fenceCacheInit();

        logger.info(">>>>>>>>>>>>>>>服务启动执行:初始化部门缓存数据<<<<<<<<<<<<");
        departmentService.departmentCacheInit();

        logger.info(">>>>>>>>>>>>>>>服务启动执行:初始化职务缓存数据<<<<<<<<<<<<");
        positionService.positionCacheInit();

        logger.info(">>>>>>>>>>>>>>>服务启动执行:初始化级别缓存数据<<<<<<<<<<<<");
        levelService.levelCacheInit();
    }

}