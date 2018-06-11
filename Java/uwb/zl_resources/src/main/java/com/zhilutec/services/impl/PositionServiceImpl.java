package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSON;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.dbs.daos.PositionDao;
import com.zhilutec.dbs.entities.Fence;
import com.zhilutec.dbs.entities.Level;
import com.zhilutec.dbs.entities.Position;
import com.zhilutec.services.IPositionService;
import com.zhilutec.services.IRedisTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class PositionServiceImpl extends IRedisTService<String> implements IPositionService {

    @Autowired
    PositionDao positionDao;

    @Override
    public void positionCacheInit() {
        Example example = new Example(Position.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isdel", 1);
        List<Position> positions = positionDao.selectByExample(example);
        for (Position position : positions) {
            String positionCode = position.getPositionCode();
            //将级别添加到缓存中
            this.redisAdd(ConstantUtil.POSITION_KEY_PRE, positionCode, JSON.toJSONString(position));
        }
    }


    @Override
    public List<Position> getPositions() {
        Example example = new Example(Level.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isdel", 1);
        List<Position> positions = positionDao.selectByExample(example);
        for (Position position : positions) {
            position.setIsdel(null);
        }
        return positions;
    }

    @Override
    public String getPositionsRs() {
        List<Position> positions = this.getPositions();
        return Result.ok(positions).toJSONString();
    }

    @Override
    protected String getRedisKey(String keyPre, Object obj) {
        return keyPre + ":" + obj.toString();
    }

    @Override
    public void redisAdd(String keyPre, String leveCode, String obj) {
        String key = this.getRedisKey(keyPre, leveCode);
        this.set(key, obj, ConstantUtil.REDIS_DEFAULT_TTL);
    }


}
