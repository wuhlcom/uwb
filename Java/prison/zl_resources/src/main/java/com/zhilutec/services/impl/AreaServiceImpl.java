/**
 * @author :wuhongliang wuhongliang@zhilutec.com
 * @version :2017年10月24日 下午6:49:05 *
 */
package com.zhilutec.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zhilutec.common.utils.RestUtil;
import com.zhilutec.db.entities.WarningStatusEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.common.utils.AbsenceUtil;
import com.zhilutec.db.daos.AreaDao;
import com.zhilutec.db.entities.AreaEntity;
import com.zhilutec.db.entities.PrisonerEntity;
import com.zhilutec.db.results.AbsenceResult;
import com.zhilutec.services.IAreaService;
import com.zhilutec.services.IPrisonerCfgService;
import com.zhilutec.services.IPrisonerService;
import com.zhilutec.services.IRedisStatusService;
import com.zhilutec.services.ITagWarningService;
import com.zhilutec.services.IWarningStatusService;

import tk.mybatis.mapper.entity.Example;

@Service
public class AreaServiceImpl implements IAreaService {

    @Autowired
    private AreaDao areaDao;

    @Resource
    private IPrisonerService prisonerService;

    @Resource
    private IPrisonerCfgService prisonerCfgService;

    @Resource
    private ITagWarningService tagWaringService;

    @Resource
    private IWarningStatusService warningStatusService;

    @Resource
    IRedisStatusService redisStatusService;

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

    /**
     * 查询所有监仓信息
     */
    @Override
    public String queryAreasResult() {
        List<AreaEntity> areas = this.queryAreas();
        System.out.println(areas);
        List areaList = new ArrayList();
        List<PrisonerEntity> prisoners = new ArrayList<>();
        for (AreaEntity areaEntity : areas) {
            Map areaMap = new HashMap();
            areaMap.put("areaName", areaEntity.getName());
            areaMap.put("areaCode", areaEntity.getAreaCode());
            areaMap.put("prisoners", prisoners);
            areaList.add(areaMap);
        }
        Map warning = warningStatusService.getWsAmount(new JSONObject());
        Map amountMap = (Map) warning.get("result");
        HashMap<Object, Object> rsMap = new HashMap<>();
        rsMap.put("areas", areaList);
        rsMap.put("warningAmount", amountMap.get("amount"));
        return Result.ok().put("result", rsMap).toJSONString();
    }

    /*
     * 查询单个监仓位置信息
     */
    @Override
    public AreaEntity queryArea(String areaCode) {
        AreaEntity area = new AreaEntity();
        area.setAreaCode(areaCode);
        AreaEntity rs = areaDao.selectOne(area);
        return rs;
    }


    /**
     * 区域页面静态信息总接口
     * 缺勤人数统计
     * 缺勤分类统计
     * 报警分级统计
     * 区域人员列表
     * 区域名
     * 区域编号
     *
     * @throws Exception
     */
    @Override
    public String getAreaStaticsInfo(String jsonStr) throws Exception {
        JSONObject paramJson = JSON.parseObject(jsonStr);
        String areaCode = paramJson.getString("areaCode");

        Map<String, Object> statisticsInfo = this.getStatisticsInfo(areaCode);
        List<PrisonerEntity> prisonerInfo = new ArrayList<>();
        try {
            AreaEntity area = this.queryArea(areaCode);
            statisticsInfo.put("areaCode", area.getAreaCode());
            statisticsInfo.put("areaName", area.getName());
            /* 区域人员列表*/
            prisonerInfo = prisonerService.queryByAreaList(jsonStr);
            statisticsInfo.put("prisoners", prisonerInfo);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Result.error(ResultCode.ROUTINE_ERR).toJSONString();
        }
        return Result.ok().put("result", statisticsInfo).toJSONString();
    }

    /*
     * 查询指定楼层区域信息
     */
    @Override
    public List<AreaEntity> queryByFloor(String floorUniqueCode) {
        Example example = new Example(AreaEntity.class);
        Example.Criteria criteria1 = example.createCriteria();
        Example.Criteria criteria2 = example.createCriteria();
        example.orderBy("sort asc");
        criteria1.andEqualTo("type", 0).orIsNull("type");
        criteria2.andEqualTo("floorUniqueCode", floorUniqueCode);
        example.and(criteria2);
        return areaDao.selectByExample(example);

    }

    /*
     * 查询指定区间楼栋信息
     */
    @Override
    public String queryResult(String jsonStr) {
        JSONObject paramJson = JSON.parseObject(jsonStr);
        String floorUniqueCode = paramJson.getString("codeValue");
        List<AreaEntity> areas = this.queryByFloor(floorUniqueCode);
        for (AreaEntity areaEntity : areas) {
            areaEntity.setId(null);
            areaEntity.setFloorUniqueCode(null);
            areaEntity.setCode(null);
            areaEntity.setRemark(null);
            areaEntity.setType(null);
        }
        return Result.ok().put("result", areas).toJSONString();
    }

    /**
     * 缺勤人数和缺勤分类统计
     **/
    private Map<String, Object> statisticsInfo(String areaCode) {
        Map<String, Object> rsMap = new HashMap<>();
        List<PrisonerEntity> prisoners = prisonerService.queryByArea(areaCode);
        Map<String, Integer> absences = new HashMap<>();
        Map<String, Integer> checkingIn = new HashMap<>();
        /**缺勤和其它是同一类*/
        int other = 0;
        // 判断当前tagId是否在配置中
        for (PrisonerEntity prisoner : prisoners) {
            String code = prisoner.getCode();
            Long tagId = prisoner.getTagId();
            int amount = prisonerCfgService.countPrisonerConf(code);
            if (amount > 0) {
                continue;
            }

            boolean absenceStatus = redisStatusService.absenceStatus(code, tagId);
            if (absenceStatus) {
                other++;
            }
        }

        /**考勤人数统计**/
        checkingIn = this.getCheckingIn(areaCode, checkingIn, other);
        /**将其它类型缺勤的合并到一起**/
        absences = this.getAbsences(areaCode, checkingIn.get("total"));

        rsMap.put("checkingIn", checkingIn);
        rsMap.put("abStatistics", absences);
        return rsMap;
    }

    /**
     * 考勤人数统计
     **/
    private Map<String, Integer> getCheckingIn(String areaCode, Map<String, Integer> checkingIn, int other) {
        int total = prisonerService.countArea(areaCode);
        int attendances = total - other;
        checkingIn.put("total", total);
        checkingIn.put("attendances", attendances);
        checkingIn.put("absence", other);
        return checkingIn;
    }

    /**
     * 缺勤分类统计
     */
    private Map<String, Integer> getAbsences(String areaCode, int other) {
        Map<String, Integer> absences;
        List<AbsenceResult> absenceResults = prisonerCfgService.absence(areaCode);
        for (AbsenceResult absenceResult : absenceResults) {
            if (absenceResult.getType().equals("9")) {
                other = other + absenceResult.getAmount();
            }
        }

        AbsenceResult absenceResult = new AbsenceResult();
        absenceResult.setType("9");
        absenceResult.setAmount(other);
        absenceResults.add(absenceResult);

        absences = this.absenceMap(absenceResults, other);
        return absences;
    }

    /**
     * 监仓页面信息
     */
    @Override
    public Map<String, Object> getStatisticsInfo(String areaCode) throws Exception {
        JSONObject paramJson = new JSONObject();
        paramJson.put("areaCode", areaCode);
        Map<String, Object> statistics = this.statisticsInfo(areaCode);
        Map<String, Long> warnings = warningStatusService.areaWarningsMap(paramJson);
        statistics.put("warning", warnings);
        return statistics;
    }

    /**
     * 处理缺勤分类
     */
    @Override
    public Map<String, Integer> absenceMap(List<AbsenceResult> absences, Integer other) {
        Map<String, Integer> rsMap = this.defaultAbsenceMap(other);
        for (AbsenceResult absenceResult : absences) {
            String type = absenceResult.getType();
            Integer amount = absenceResult.getAmount();
            if (type.equals(AbsenceUtil.HOSPITAL)) {
                rsMap.put(AbsenceUtil.HOSPITAL, amount);
            } else if (type.equals(AbsenceUtil.CELL)) {
                rsMap.put(AbsenceUtil.CELL, amount);
            } else if (type.equals(AbsenceUtil.MEET)) {
                rsMap.put(AbsenceUtil.MEET, amount);
            } else if (type.equals(AbsenceUtil.SINGLE)) {
                rsMap.put(AbsenceUtil.SINGLE, amount);
            } else if (type.equals(AbsenceUtil.OUTSIDE)) {
                rsMap.put(AbsenceUtil.OUTSIDE, amount);
            } else if (type.equals(AbsenceUtil.PUNISHMENT)) {
                rsMap.put(AbsenceUtil.PUNISHMENT, amount);
            } else if (type.equals(AbsenceUtil.SEE_DOCTOR)) {
                rsMap.put(AbsenceUtil.SEE_DOCTOR, amount);
            } else if (type.equals(AbsenceUtil.TEACHING_BUILDING)) {
                rsMap.put(AbsenceUtil.TEACHING_BUILDING, amount);
            } else {
                rsMap.put(AbsenceUtil.OTHER, amount);
            }
        }
        return rsMap;
    }

    /**
     * 默认缺勤分类信息，other的默认值应与监仓的总人数一致
     */
    private Map<String, Integer> defaultAbsenceMap(Integer other) {
        Map<String, Integer> rsMap = new HashMap<>();
        rsMap.put(AbsenceUtil.HOSPITAL, 0);
        rsMap.put(AbsenceUtil.CELL, 0);
        rsMap.put(AbsenceUtil.MEET, 0);
        rsMap.put(AbsenceUtil.OTHER, other);
        rsMap.put(AbsenceUtil.OUTSIDE, 0);
        rsMap.put(AbsenceUtil.PUNISHMENT, 0);
        rsMap.put(AbsenceUtil.SEE_DOCTOR, 0);
        rsMap.put(AbsenceUtil.TEACHING_BUILDING, 0);
        rsMap.put(AbsenceUtil.SINGLE, 0);
        return rsMap;
    }

    /**
     * 监仓信息，考勤人数，考勤分类统计，报警分级，监仓人员
     */
    @Override
    public String getAreaInfo(JSONObject jsonObj) throws Exception {
        String areaCode = jsonObj.getString("areaCode");
        AreaEntity area = this.queryArea(areaCode);
        String areaName = area.getName();

        List<PrisonerEntity> prisonerLs = new ArrayList<>();
        Map<String, Integer> checkingIn = new HashMap<>();
        Map<String, Long> warnings = new HashMap<>();
        Map<String, Integer> absences = new HashMap<>();
        List<WarningStatusEntity> crossList = new ArrayList();

        Map<String, Object> rsMap = new HashMap<>();
        // 初始化返回结果
        rsMap.put("areaCode", areaCode);
        rsMap.put("areaName", areaName);
        rsMap.put("cross", crossList);
        Map<String,Object> websocketResult = RestUtil.getAreaInfo(jsonObj);
        Integer errcode = (Integer) websocketResult.get("errcode");
        /**如果返回值就给前端一个默认的监仓页面信息*/
        if (errcode.intValue()==ResultCode.SUCCESS.getCode()) {
            rsMap = (Map<String, Object>) websocketResult.get("result");
        } else {
            //无法从websocket获取正常考勤时给定一个默认的考勤信息
            checkingIn = this.defaultCheckingIn(areaCode);
            absences = this.defaultAbsenceMap(checkingIn.get("total"));
            warnings = warningStatusService.areaWarningsMap(jsonObj);
            prisonerLs = prisonerService.getPrisoners(areaCode);
            rsMap.put("checkingIn", checkingIn);
            rsMap.put("abStatistics", absences);
            rsMap.put("warnings", warnings);
            rsMap.put("prisoners", prisonerLs);
            rsMap.put("remote","failed");//表示远程请求失败
        }

        return Result.ok(ResultCode.SUCCESS.getCode(), rsMap).toJSONString();
    }

    /**
     * 考勤人数统计
     **/
    private Map defaultCheckingIn(String areaCode) {
        Map checkingInMap = new HashMap();
        int total = prisonerService.countArea(areaCode);
        checkingInMap.put("total", total);
        checkingInMap.put("attendances", 0);
        checkingInMap.put("absence", total);
        return checkingInMap;
    }

}