package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.dbs.entities.Fence;
import com.zhilutec.services.IFenceService;
import com.zhilutec.services.IRedisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class FenceServiceImpl implements IFenceService {

    @Resource
    IRedisService redisService;

    @Override
    public Fence getCache(String fenceCode) {
        Fence fence = null;

        String key = redisService.genRedisKey(ConstantUtil.FENCE_KEY_PRE, fenceCode);
        Map map = redisService.hashGetMap(key);

        if(map==null){
            return fence;
        }
        String mapStr=JSONObject.toJSONString(map);

        fence = JSONObject.parseObject(mapStr, Fence.class);
        return fence;
    }

}
