package com.zhilutec.uwb.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.uwb.entity.Level;
import com.zhilutec.uwb.service.ILevelService;
import com.zhilutec.uwb.service.IRedisService;
import com.zhilutec.uwb.util.ConstantUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service("levelServiceImpl")
public class LevelServiceImpl implements ILevelService {
    @Resource
    IRedisService redisService;

    @Override
    public Level getCache(String levelCode) {
        Level level = null;

        String key = redisService.genRedisKey(ConstantUtil.LEVEL_KEY_PRE, levelCode);
        Map map = redisService.hashGetMap(key);

        if (map == null) {
            return level;
        }
        String mapStr = JSONObject.toJSONString(map);

        level = JSONObject.parseObject(mapStr, Level.class);
        return level;
    }
}
