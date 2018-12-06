package com.zhilutec.uwb.dao;



import com.zhilutec.uwb.common.MyMapper;
import com.zhilutec.uwb.entity.Warning;
import com.zhilutec.uwb.common.pojo.WarningLevelRs;
import com.zhilutec.uwb.common.pojo.WarningRs;

import java.util.List;
import java.util.Map;

public interface WarningDao extends MyMapper<Warning>{
    List<Warning> getWarnings(Warning warning);

    List<Map<String,Integer>> warningStatistics(Map<String, Long> map);

    List<WarningRs> topPersons(Integer limit);

    List<WarningLevelRs> topWarnings(Map<String, Object> map);
}
