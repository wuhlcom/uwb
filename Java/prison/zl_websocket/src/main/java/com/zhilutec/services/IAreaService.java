package com.zhilutec.services;


import java.util.List;
import java.util.Map;

import com.zhilutec.db.entities.AreaEntity;
import com.zhilutec.db.results.AbsenceResult;

public interface IAreaService {

	AreaEntity queryArea(String areaCode);

	Map<String, Integer> absenceMap(List<AbsenceResult> absences);

    /*
* 查询所有监仓信息
*/
    List<AreaEntity> queryAreas();

    /*
     * 查询所有监仓信息
     */
    List<AreaEntity> queryAll();

    Map<String, Object> getStatisticsInfo(String areaCode) throws Exception;

}
