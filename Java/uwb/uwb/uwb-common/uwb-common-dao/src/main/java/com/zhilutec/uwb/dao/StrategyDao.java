package com.zhilutec.uwb.dao;


import com.zhilutec.uwb.common.MyMapper;
import com.zhilutec.uwb.entity.Strategy;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;
import java.util.Map;

@Mapper
public interface StrategyDao extends MyMapper<Strategy>{
    List<Strategy> getStrategies(Strategy strategy);

    List<Strategy> getStrategiesByDpt(Map map);
}

