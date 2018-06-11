package com.zhilutec.services;

import java.util.List;
import java.util.Map;

import com.zhilutec.db.entities.PrisonerCfgEntity;
import com.zhilutec.db.results.AbsenceResult;

public interface IPrisonerCfgService {

	PrisonerCfgEntity query(String jsonStr);

	int countPrisonerConf(String code);

	List<PrisonerCfgEntity> areaQuery(String jsonStr);

	int areaCount(String jsonStr);

	List<AbsenceResult> absence(String areaCode);
    Map<String, Integer> abStatistics(String areaCode, Integer cther);
}
