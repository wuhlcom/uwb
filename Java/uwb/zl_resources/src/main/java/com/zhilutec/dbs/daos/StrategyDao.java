package com.zhilutec.dbs.daos;

import com.zhilutec.common.mappers.MyMapper;
import com.zhilutec.dbs.entities.Strategy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface StrategyDao extends MyMapper<Strategy>, BaseDao<Strategy> {
    List<Strategy> getStrategies(Strategy strategy);

    List<Strategy> getStrategiesByDpt(Map map);
}

