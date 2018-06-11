package com.zhilutec.services.impl;

import com.zhilutec.services.ILevelService;
import com.zhilutec.services.IRedisTService;
import org.springframework.stereotype.Service;

@Service("levelServiceImpl")
public class LevelServiceImpl extends IRedisTService<String> implements ILevelService {

    @Override
    protected String getRedisKey(String keyPre, Object obj) {
        return keyPre + ":" + obj.toString();
    }

    @Override
    public String redisGet(String keyPre, String levelCode) {
        String key = this.getRedisKey(keyPre, levelCode);
        return this.getValue(key);
    }
}
