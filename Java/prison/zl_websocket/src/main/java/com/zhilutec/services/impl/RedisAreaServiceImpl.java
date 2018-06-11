package com.zhilutec.services.impl;

import com.zhilutec.db.entities.AreaEntity;
import com.zhilutec.services.IAreaService;
import com.zhilutec.services.IRedisAreaService;
import com.zhilutec.services.IRedisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RedisAreaServiceImpl extends IRedisService<AreaEntity> implements IRedisAreaService{

    private static final String AREA_REDIS_KEY = "AREA_INFO";

    @Override
    protected String getRedisKey() {
        return AREA_REDIS_KEY;
    }

    @Resource
    private IAreaService areaService;

    @Override
    public void put() {
        List<AreaEntity> areas = areaService.queryAll();
        for (AreaEntity area : areas ) {
             put(area.getAreaCode(),area,-1);
        }
    }

    @Override
    public AreaEntity get(String field) {
        return super.get(field);
    }
}
