package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.dbs.daos.PositionDao;
import com.zhilutec.dbs.entities.Level;
import com.zhilutec.dbs.entities.Position;
import com.zhilutec.services.IPositionService;
import com.zhilutec.services.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class PositionServiceImpl implements IPositionService {

    @Autowired
    PositionDao positionDao;


    @Resource
    IRedisService redisService;

    @Override
    public void positionCacheInit() {
        Example example = new Example(Position.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isdel", 1);
        List<Position> positions = positionDao.selectByExample(example);
        for (Position position : positions) {
            // String positionCode = position.getPositionCode();
            // //将级别添加到缓存中
            // this.redisAdd(ConstantUtil.POSITION_KEY_PRE, positionCode, JSON.toJSONString(position));
            this.addPositionCache(position);
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


    private void addPositionCache(Position position) {
        String positionCode = position.getPositionCode();
        //将区域添加到缓存中
        String keyPre = ConstantUtil.POSITION_KEY_PRE;
        String key = redisService.genRedisKey(keyPre, positionCode);

        String str = JSON.toJSONString(position);
        Map map = JSONObject.parseObject(str, Map.class);
        redisService.hashAddMap(key, map, ConstantUtil.REDIS_DEFAULT_TTL);
    }


}
