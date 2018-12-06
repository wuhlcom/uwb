package com.zhilutec.uwb.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.uwb.entity.Fence;
import com.zhilutec.uwb.service.IFenceService;
import com.zhilutec.uwb.service.IRedisService;
import com.zhilutec.uwb.util.ConstantUtil;
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

        if (map == null) {
            return fence;
        }
        String mapStr = JSONObject.toJSONString(map);

        fence = JSONObject.parseObject(mapStr, Fence.class);
        return fence;
    }

}
