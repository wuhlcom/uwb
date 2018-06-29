package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.dbs.daos.LevelDao;
import com.zhilutec.dbs.entities.Level;
import com.zhilutec.dbs.entities.Position;
import com.zhilutec.services.ILevelService;
import com.zhilutec.services.IPositionService;
import com.zhilutec.services.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LevelServiceImpl implements ILevelService {

    @Autowired
    LevelDao levelDao;

    @Resource
    IPositionService positionService;

    @Resource
    IRedisService redisService;

    @Override
    public void levelCacheInit() {
        Example example = new Example(Level.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isdel", 1);
        List<Level> levels = levelDao.selectByExample(example);
        for (Level level : levels) {

            //将级别添加到缓存中
            // String levelCode = level.getLevelCode();
            // this.redisAdd(ConstantUtil.LEVEL_KEY_PRE, levelCode, JSON.toJSONString(level));
            this.addLevelCache(level);
        }
    }

    //只有两级级别，查询级别
    @Override
    public List<Level> getLevels() {
        Example example = new Example(Level.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isdel", 1).andEqualTo("parentCode", "0");
        List<Level> levels = levelDao.selectByExample(example);

        for (Level level : levels) {
            Example example1 = new Example(Level.class);
            Example.Criteria criteria1 = example1.createCriteria();

            String parentCode = level.getLevelCode();
            criteria1.andEqualTo("isdel", 1).andEqualTo("parentCode", parentCode);
            List<Level> subLevels = levelDao.selectByExample(example1);
            for (Level subLevel : subLevels) {
                subLevel.setIsdel(null);
            }
            level.setChildren(subLevels);
            level.setIsdel(null);
        }
        return levels;
    }


    @Override
    public String getLevelsRs() {
        List<Level> ranks = this.getLevels();
        return Result.ok(ranks).toJSONString();
    }


    @Override
    public String getlevel() {
        Map<String, List> resultMap = new HashMap<>();
        List<Level> levels = this.getLevels();
        List<Position> positions = positionService.getPositions();
        resultMap.put("levels", levels);
        resultMap.put("positions", positions);
        return Result.ok(resultMap).toJSONString();
    }

    private void addLevelCache(Level level) {
        String levelCode = level.getLevelCode();
        //将区域添加到缓存中
        String keyPre = ConstantUtil.LEVEL_KEY_PRE;
        String key = redisService.genRedisKey(keyPre, levelCode);
        String str = JSON.toJSONString(level);
        Map map = JSONObject.parseObject(str, Map.class);
        redisService.hashAddMap(key, map, ConstantUtil.REDIS_DEFAULT_TTL);
    }
}
