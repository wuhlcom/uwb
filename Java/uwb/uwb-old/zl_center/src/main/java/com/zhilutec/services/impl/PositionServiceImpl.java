package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.dbs.entities.Position;
import com.zhilutec.services.IPositionService;
import com.zhilutec.services.IRedisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service("positionServiceImpl")
public class PositionServiceImpl implements IPositionService {
    @Resource
    IRedisService redisService;

    @Override
    public Position getCache(String positionCode) {
        Position position = null;

        String key = redisService.genRedisKey(ConstantUtil.POSITION_KEY_PRE, positionCode);
        Map map = redisService.hashGetMap(key);


        if(map==null){
            return position;
        }
        String mapStr=JSONObject.toJSONString(map);

        position = JSONObject.parseObject(mapStr, Position.class);
        return position;
    }
}
