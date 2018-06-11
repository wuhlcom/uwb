package com.zhilutec.services.impl;

import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.services.IRedisTService;
import com.zhilutec.services.IStatusService;
import org.springframework.stereotype.Service;


@Service
public class StatusServiceImpl extends IRedisTService<String> implements IStatusService {

    @Override
    protected String getRedisKey(String keyPre, Object obj) {
        String tagId = Long.toString((Long) obj);
        return keyPre + ":" + tagId;
    }

    @Override
    public void redisAdd(String keyPre, Long tagId, String msg) {
        String key = this.getRedisKey(keyPre, tagId);
        this.set(key, msg, ConstantUtil.REDIS_DEFAULT_TTL);
    }

    @Override
    public void redisDel(String keyPre, Long tagId) {
        String key = this.getRedisKey(keyPre, tagId);
        this.delete(key);
    }

    @Override
    public String redisGet(String keyPre, Long tagId) {
        String key = this.getRedisKey(keyPre, tagId);
        return this.getValue(key);
    }
}
