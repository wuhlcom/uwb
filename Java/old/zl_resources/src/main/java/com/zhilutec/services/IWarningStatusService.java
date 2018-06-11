package com.zhilutec.services;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.db.results.WarningStatusResult;

public interface IWarningStatusService {
    String getAreaWarnings(String jsonStr) throws Exception;

    Map<String, Long> areaWarningsMap(String jsonStr) throws Exception;

    List<WarningStatusResult> getPrisonerWarnings(String jsonStr);

    int countPrisonerWarnings(JSONObject jsonObj);

    String getAreaAbsence(String jsonStr);

    int countWarnings();

    String getWariningList(String jsonStr);

    String getPrisonerWarningsList(String jsonStr);

    String updateWarning(String jsonStr);

    Map<String, Long> areaWarningsMap(JSONObject paramJson) throws Exception;

    Map<String, Object> getWsAmount(JSONObject jsonObject);

    String resetWsAmount(JSONObject jsonObject);
}
