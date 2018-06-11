package com.zhilutec.services.impl;


import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.dbs.entities.Coordinate;
import com.zhilutec.services.ICoordinateService;
import com.zhilutec.services.IRedisService;
import org.springframework.stereotype.Service;

@Service
public class CoordinateServiceImpl extends IRedisService<Coordinate> implements ICoordinateService {

    /**
     * @param key   tagId+strategyCode
     * @param field level
     */
    @Override
    public Coordinate getReidsCoordinate(String key, String field) {
        return this.get(key, field);
    }

    @Override
    public Coordinate getReidsCoordinate(Long tagId) {
        String tid = Long.toString(tagId);
        String warningKey = ConstantUtil.COORDINATE_KEY_PRE + ":" + tid;
        return this.getReidsCoordinate(warningKey, tid);
    }

    @Override
    public void deleteRedisCoordinate(String key, String field) {
        this.remove(key, field);
    }


    @Override
    public void deleteRedisCoordinate(Long tagId) {
        String tid = Long.toString(tagId);
        String warningKey = ConstantUtil.COORDINATE_KEY_PRE + ":" + tid;
        this.deleteRedisCoordinate(warningKey, tid);
    }

    @Override
    public void addRedisCoordinate(Coordinate coordinate, Long expire) {
        String tagId = Long.toString(coordinate.getTagId());
        String coordianteKey = ConstantUtil.COORDINATE_KEY_PRE + ":" + tagId;
        this.put(coordianteKey, tagId, coordinate, expire);
    }

    @Override
    protected String getRedisKey() {
        return null;
    }

}
