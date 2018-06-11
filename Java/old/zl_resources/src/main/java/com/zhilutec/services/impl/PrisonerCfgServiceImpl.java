package com.zhilutec.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.db.daos.PrisonerCfgDao;
import com.zhilutec.db.entities.PrisonerCfgEntity;
import com.zhilutec.db.entities.PrisonerEntity;
import com.zhilutec.db.results.AbsenceResult;
import com.zhilutec.services.IAreaService;
import com.zhilutec.services.IPrisonerCfgService;
import com.zhilutec.services.IPrisonerService;
import com.zhilutec.services.IRedisStatusService;

import tk.mybatis.mapper.entity.Example;

@Service
public class PrisonerCfgServiceImpl implements IPrisonerCfgService {

    @Autowired
    PrisonerCfgDao prisonerCfgDao;

    @Resource
    IPrisonerService prisonerService;

    @Resource
    IRedisStatusService redisStatusService;

    @Resource
    IAreaService areaService;

    /**
     * 监仓缺勤查询
     **/
    @Override
    public List<PrisonerCfgEntity> areaQuery(String jsonStr) {
        JSONObject paramObj = JSON.parseObject(jsonStr);
        Example example = new Example(PrisonerCfgEntity.class);
        Example.Criteria recordCriteria = example.createCriteria();
        recordCriteria.andEqualTo("available", 1).andEqualTo("areaCode", paramObj.getString("areaCode"));
        return prisonerCfgDao.selectByExample(example);
    }

    /**
     * 监仓缺勤总数统计
     **/
    @Override
    public int areaCount(String jsonStr) {
        JSONObject paramObj = JSON.parseObject(jsonStr);
        Example example = new Example(PrisonerCfgEntity.class);
        Example.Criteria recordCriteria = example.createCriteria();
        recordCriteria.andEqualTo("available", 1).andEqualTo("areaCode", paramObj.getString("areaCode"));
        return prisonerCfgDao.selectCountByExample(example);
    }

    /**
     * 单人有效缺勤数量查询
     */
    @Override
    public int countPrisonerConf(String code) {
        PrisonerCfgEntity params = new PrisonerCfgEntity();
        params.setAvailable(1);
        params.setPrisonerCode(code);
        return prisonerCfgDao.selectCount(params);
    }

    /* 单人缺勤查询 **/
    @Override
    public PrisonerCfgEntity query(String jsonStr) {
        JSONObject paramObj = JSON.parseObject(jsonStr);
        String code = paramObj.getString("code");
        PrisonerCfgEntity params = new PrisonerCfgEntity();
        params.setAvailable(1);

        if (code != null && !code.trim().isEmpty()) {
            params.setPrisonerCode(code);
        } else {
            System.out.println("参数错误");
            return null;
        }
        return prisonerCfgDao.selectOne(params);
    }

    /**
     * 单个监仓缺勤统计
     */
    @Override
    public List<AbsenceResult> absence(String areaCode) {
        List<AbsenceResult> rs = prisonerCfgDao.absenceStatistics(areaCode);
        return rs;
    }

    /**
     * 缺勤分类统计中处理其它类型的缺勤数据
     * 在缺勤配置中不存在的人就查询状态机来判断是否缺勤
     * 并将状态机中查询的结果与配置中的结果汇总
     **/
    @Override
    public Map<String, Integer> abStatistics(String areaCode, Integer cther) {
        List<PrisonerEntity> prisoners = prisonerService.queryByArea(areaCode);
        int other = 0;
        // 判断当前tagId是否在配置中
        for (PrisonerEntity prisoner : prisoners) {
            String code = prisoner.getCode();
            Long tagId = prisoner.getTagId();
            int amount = this.countPrisonerConf(code);
            if (amount > 0) {
                continue;
            }
            if (redisStatusService.absenceStatus(code, tagId)) {
                other++;
            }
        }

        /**将其它类型缺勤的合并到一起**/
        List<AbsenceResult> absenceResults = this.absence(areaCode);
        for (AbsenceResult absenceResult : absenceResults) {
            if (absenceResult.getType().equals("9")) {
                other = other + absenceResult.getAmount();
            }
        }

        AbsenceResult absenceResult = new AbsenceResult();
        absenceResult.setType("9");
        absenceResult.setAmount(other);

        // 找不到监仓会返回空
        if (!absenceResults.isEmpty()) {
            absenceResults.add(absenceResult);
        }
        return areaService.absenceMap(absenceResults, other);
    }

    /***缺勤列表**/
    public List<HashMap<String, Object>> absenceList(String areaCode) {
        return prisonerCfgDao.absenceList(areaCode);
    }

}
