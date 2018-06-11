package com.zhilutec.services.impl;

import com.zhilutec.services.IPositionService;
import com.zhilutec.services.IRedisTService;
import org.springframework.stereotype.Service;

@Service("positionServiceImpl")
public class PositionServiceImpl extends IRedisTService<String> implements IPositionService {

    @Override
    protected String getRedisKey(String keyPre, Object obj) {
        return keyPre + ":" + obj.toString();
    }

    @Override
    public String redisGet(String keyPre, String positionCode) {
        String key = this.getRedisKey(keyPre, positionCode);
        return this.getValue(key);
    }

}
