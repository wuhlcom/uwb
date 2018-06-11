package com.zhilutec.services.impl;

import com.zhilutec.db.entities.PrisonerEntity;
import com.zhilutec.services.IPrisonerService;
import com.zhilutec.services.IRedisPrisonerService;
import com.zhilutec.services.IRedisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RedisPrisonerServiceImpl extends IRedisService<PrisonerEntity> implements IRedisPrisonerService {

    private static final String AREA_REDIS_KEY = "PRISONER_INFO";
    @Override
    protected String getRedisKey() {
        return AREA_REDIS_KEY;
    }

    @Resource
    private IPrisonerService prisonerService;

    @Override
    public void put() {
        List<PrisonerEntity> prisoners = prisonerService.queryAll();
        for (PrisonerEntity prisoner : prisoners ) {
             put(prisoner.getTagId().toString(),prisoner,-1);
        }
    }

    @Override
    public PrisonerEntity get(String field) {
        return super.get(field);
    }
}
