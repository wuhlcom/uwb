/**
 * @author :wuhongliang wuhongliang@zhilutec.com
 * @version :2017年10月24日 下午6:49:05 *
 */
package com.zhilutec.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhilutec.db.daos.AreaDao;
import com.zhilutec.db.entities.AreaEntity;
import com.zhilutec.services.IAreaService;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;


@Service
@Transactional
public class AreaServiceImpl implements IAreaService {

    @Autowired
    private AreaDao areaDao;


    /** 查询单个监仓位置信息
     */
    @Override
    public AreaEntity queryArea(String areaCode) {
        AreaEntity area = new AreaEntity();
        area.setAreaCode(areaCode);
        return areaDao.selectOne(area);
    }

    /*
     * 查询所有监仓信息
     */
    @Override
    public List<AreaEntity> queryAreas() {
        Example example = new Example(AreaEntity.class);
        Example.Criteria recordCriteria = example.createCriteria();
        example.orderBy("sort asc");
        recordCriteria.andEqualTo("type", 0).orIsNull("type");
        return areaDao.selectByExample(example);
    }

    /*
     * 查询所有区域信息
     */
    @Override
    public List<AreaEntity> queryAll() {
        return areaDao.selectAll();
    }


}