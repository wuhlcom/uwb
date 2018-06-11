package com.zhilutec.services;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.db.entities.AreaEntity;
import com.zhilutec.db.results.AbsenceResult;

public interface IAreaService {

	List<AreaEntity> queryAreas();

	AreaEntity queryArea(String areaCode);

	String queryAreasResult();

	List<AreaEntity> queryByFloor(String floorUniqueCode);

	String queryResult(String floorUniqueCode);

	Map<String, Object> getStatisticsInfo(String areaCode) throws Exception;

	String getAreaStaticsInfo(String jsonStr) throws Exception;


    Map<String, Integer> absenceMap(List<AbsenceResult> absences, Integer other);

	String getAreaInfo(JSONObject jsonObj) throws Exception;
}
