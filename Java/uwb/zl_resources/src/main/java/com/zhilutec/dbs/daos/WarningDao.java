package com.zhilutec.dbs.daos;

import com.zhilutec.common.mappers.MyMapper;
import com.zhilutec.dbs.entities.Warning;
import com.zhilutec.dbs.pojos.WarningLevelRs;
import com.zhilutec.dbs.pojos.WarningRs;

import java.util.List;
import java.util.Map;

public interface WarningDao extends MyMapper<Warning>, BaseDao<Warning> {
    List<Warning> getWarnings(Warning warning);

    List<Map<String,Integer>> warningStatistics(Map<String,Long> map);

    List<WarningRs> topPersons(Integer limit);

    List<WarningLevelRs> topWarnings(Map<String,Object> map);
}
