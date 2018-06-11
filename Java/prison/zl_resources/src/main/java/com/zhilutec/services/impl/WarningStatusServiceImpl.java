package com.zhilutec.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.druid.support.json.JSONUtils;
import com.zhilutec.common.utils.RestUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilutec.db.daos.WarningStatusDao;
import com.zhilutec.db.entities.WarningStatusEntity;
import com.zhilutec.db.results.WarningStatusResult;
import com.zhilutec.services.IPrisonerService;
import com.zhilutec.services.IWarningStatusService;

import tk.mybatis.mapper.entity.Example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.common.utils.WarningUtil;
import com.zhilutec.common.utils.ZlTimeUtil;
import com.zhilutec.common.validators.WarningValidator;


@Service
@Transactional
public class WarningStatusServiceImpl implements IWarningStatusService {

    @Autowired
    private WarningStatusDao warningStatusDao;

    @Resource
    private IPrisonerService prisonerService;

    /**
     * 区域报警分类统计
     *
     * @return
     * @throws Exception
     **/
    private List<Map<String, Object>> warningStatistics(String jsonStr) throws Exception {
        JSONObject paramJson = JSON.parseObject(jsonStr);
        return this.warningStatistics(paramJson);
    }

    private List<Map<String, Object>> warningStatistics(JSONObject paramJson) throws Exception {
        Long startTime = paramJson.getLong("startTime");
        Long endTime = paramJson.getLong("endTime");
        if (startTime == null) {
            startTime = ZlTimeUtil.specStamp();
            paramJson.put("startTime", startTime);
        }
        if (endTime == null) {
            endTime = System.currentTimeMillis() / 1000;
            paramJson.put("endTime", endTime);
        }
        return warningStatusDao.areaWarningStatistics(paramJson);
    }

    /**
     * 区域报警分类统计
     **/
    @Override
    public Map<String, Long> areaWarningsMap(String jsonStr) throws Exception {
        Map<String, Long> rsMap = new HashMap<String, Long>();
        rsMap.put(WarningUtil.URGENCY_CODE, 0L);
        rsMap.put(WarningUtil.COMMON_CODE, 0L);
        rsMap.put(WarningUtil.TIP_CODE, 0L);

        List<Map<String, Object>> warnings = this.warningStatistics(jsonStr);
        for (Map<String, Object> map : warnings) {
            String key = (String) map.get("level");
            Long value = (Long) map.get("amount");
            if (key.equals(WarningUtil.URGENCY_CODE)) {
                rsMap.put(WarningUtil.URGENCY_CODE, value);
            } else if (key.equals(WarningUtil.COMMON_CODE)) {
                rsMap.put(WarningUtil.COMMON_CODE, value);
            } else if (key.equals(WarningUtil.TIP_CODE)) {
                rsMap.put(WarningUtil.TIP_CODE, value);
            }
        }
        return rsMap;
    }

    /**
     * 区域报警分级统计
     **/
    @Override
    public Map<String, Long> areaWarningsMap(JSONObject paramJson) throws Exception {
        return this.areaWarningsMap(paramJson.toJSONString());
    }

    /**
     * 区域报警列表并分页
     *
     * @throws Exception
     */
    @Override
    public String getAreaWarnings(String jsonStr) throws Exception {
        Result rs = WarningValidator.warningQueryVal(jsonStr);
        if ((Integer) rs.get("errcode") != 10001) {
            return rs.toJSONString();
        }
        JSONObject paramJson = JSON.parseObject(jsonStr);

        Integer page = paramJson.getInteger("page");
        Integer listRows = paramJson.getInteger("listRows");
        String order = paramJson.getString("order");
        if (order == null || order.isEmpty()) {
            order = "desc";
            paramJson.put("order", order);
        }

        /** 初始化分页参数 */
        page = page == null ? 1 : page;
        listRows = listRows == null ? 10 : listRows;

        /** 进行分页 */
        Page<WarningStatusResult> pageObj = PageHelper.startPage(page, listRows, true);
        List<WarningStatusResult> warnings = warningStatusDao.areaWarningsList(paramJson);

        warnings = this.getFinishTime(warnings);
        /* finishTime为空的设置其值为0 **/
        for (WarningStatusResult warningState : warnings) {
            Long finishTime = warningState.getFinishTime();
            if (finishTime == null) {
                finishTime = 0L;
                warningState.setFinishTime(finishTime);
            }
        }
        /* 获取总数 */
        long count = pageObj.getTotal();

        Map<String, List<WarningStatusResult>> warningMap = new HashMap<>();
        Map<String, Object> rsMap = new HashMap<>();
        warningMap.put("warnings", warnings);
        rsMap.put("result", warningMap);
        rsMap.put("totalRows", count);
        return Result.ok(rsMap).toJSONString();
    }

    /**
     * 单个囚犯报警列表
     ***/
    @Override
    public List<WarningStatusResult> getPrisonerWarnings(String jsonStr) {
        JSONObject paramJson = JSON.parseObject(jsonStr);
        List<WarningStatusResult> warnings = warningStatusDao.prisonerWarnings(paramJson);

		/* 处理报警取消时间 */
        warnings = this.getFinishTime(warnings);
        warnings = this.modifyResult(warnings);
        return warnings;
    }

    /**
     * 单个囚犯报警列表并分页显示
     ***/
    @Override
    public String getPrisonerWarningsList(String jsonStr) {
        JSONObject paramJson = JSON.parseObject(jsonStr);
        Integer page = paramJson.getInteger("page");
        Integer listRows = paramJson.getInteger("listRows");
        String order = paramJson.getString("order");
        if (order == null || order.isEmpty()) {
            order = "desc";
            paramJson.put("order", order);
        }

        /** 初始化分页参数 */
        page = page == null ? 1 : page;
        listRows = listRows == null ? 10 : listRows;

        /** 进行分页 */
        Page<WarningStatusResult> pageObj = PageHelper.startPage(page, listRows);
        List<WarningStatusResult> warnings = warningStatusDao.prisonerWarnings(paramJson);
        /* 获取总数 */
        long count = pageObj.getTotal();
        /* 处理报警取消时间 */
        warnings = this.getFinishTime(warnings);
        warnings = this.modifyResult(warnings);

        Map<String, List<WarningStatusResult>> warningMap = new HashMap<>();
        Map<String, Object> rsMap = new HashMap<>();
        warningMap.put("warnings", warnings);
        rsMap.put("result", warningMap);
        rsMap.put("totalRows", count);
        return Result.ok(rsMap).toJSONString();
    }

    private List<WarningStatusResult> modifyResult(List<WarningStatusResult> warnings) {
        for (WarningStatusResult warningState : warnings) {
            Long finishTime = warningState.getFinishTime();
            if (finishTime == null) {
                finishTime = 0L;
                warningState.setFinishTime(finishTime);
            }
            warningState.setName(null);
            warningState.setTagId(null);
        }
        return warnings;
    }

    /**
     * 单个囚犯统计报警数量
     **/
    @Override
    public int countPrisonerWarnings(JSONObject jsonObj) {
        String code = jsonObj.getString("code");
        Example example = new Example(WarningStatusEntity.class);
        Example.Criteria criteria1 = example.createCriteria();
        Example.Criteria criteria2 = example.createCriteria();
        criteria1.andEqualTo("status", 0).orEqualTo("op", 1);
        criteria2.andLike("code", code);
        /** 相当于(a=x or b=y) and c like '%ccc%' ***/
        example.and(criteria2);
        return warningStatusDao.selectCountByExample(example);
    }

    /**
     * 所有未处理报警数量
     **/
    @Override
    public int countWarnings() {
        Example example = new Example(WarningStatusEntity.class);
        Example.Criteria criteria1 = example.createCriteria();
        Example.Criteria criteria2 = example.createCriteria();
        criteria1.andEqualTo("status", 0).orEqualTo("op", 1);
        criteria2.andEqualTo("state", 0);
        /** 相当于(a=x or b=y) and c = 'c' ***/
        example.and(criteria2);
        return warningStatusDao.selectCountByExample(example);
    }

    /**
     * 1 找出所有缺勤(串仓，电子围栏，离线，信号消失) 2 从所有缺勤中取一条，找出其所有时间戳大于该缺勤时间的中时间值最小的即为其恢复时间 3
     * 将恢复时间和该缺勤记录合成一条数据
     */

    @Override
    public String getAreaAbsence(String jsonStr) {
        JSONObject paramJson = JSON.parseObject(jsonStr);

        Integer page = paramJson.getInteger("page");
        Integer listRows = paramJson.getInteger("listRows");
        String order = paramJson.getString("order");
        if (order == null || order.isEmpty()) {
            order = "desc";
            paramJson.put("order", order);
        }

        /** 初始化分页参数 */
        page = page == null ? 1 : page;
        listRows = listRows == null ? 10 : listRows;

        /** 进行分页 */
        Page<WarningStatusResult> pageObj = PageHelper.startPage(page, listRows);
        List<WarningStatusResult> absence = warningStatusDao.areaAbsenceList(paramJson);
		/* 获取总数 */
        long count = pageObj.getTotal();
		/* 处理报警取消时间 */
        absence = this.getFinishTime(absence);

		/* finishTime为空的设置其值为0 **/
        for (WarningStatusResult warningState : absence) {
            Long finishTime = warningState.getFinishTime();
            if (finishTime == null) {
                finishTime = 0L;
                warningState.setFinishTime(finishTime);
            }
        }

        Map<String, List<WarningStatusResult>> absenceMap = new HashMap<>();
        Map<String, Object> rsMap = new HashMap<>();
        absenceMap.put("absence", absence);
        rsMap.put("result", absenceMap);
        rsMap.put("totalRows", count);
        return Result.ok(rsMap).toJSONString();
    }

    /**
     * 获取报警的自动取消时间
     */
    private List<WarningStatusResult> getFinishTime(List<WarningStatusResult> absence) {
        RowBounds rowBounds = new RowBounds(0, 1);
        for (WarningStatusResult warningStatus : absence) {
            Example example = new Example(WarningStatusEntity.class);
            Example.Criteria criteria1 = example.createCriteria();

            Long timestamp = warningStatus.getTimestamp();
            String warningCode = warningStatus.getWarningCode();
            Integer type = warningStatus.getType();

            List<WarningStatusEntity> cancelRecord = null;
            if (type == 3) {
                criteria1.andEqualTo("op", 0).andEqualTo("warningCode", warningCode).andGreaterThan("timestamp",
                        timestamp);
                example.orderBy("timestamp asc");
                cancelRecord = warningStatusDao.selectByExampleAndRowBounds(example, rowBounds);
            }
            if (cancelRecord != null && !cancelRecord.isEmpty()) {
                warningStatus.setFinishTime(cancelRecord.get(0).getTimestamp());
            }
        }
        return absence;
    }

    /**
     * 所有报警列表
     **/
    @Override
    public String getWariningList(String jsonStr) {
        JSONObject paramJson = JSONObject.parseObject(jsonStr);
        Integer page = paramJson.getInteger("page");
        Integer listRows = paramJson.getInteger("listRows");
        String order = paramJson.getString("order");
        if (order == null || order.isEmpty()) {
            order = "desc";
            paramJson.put("order", order);
        }
        /** 进行分页 */
        /** 初始化分页参数 */
        page = page == null ? 1 : page;
        listRows = listRows == null ? 10 : listRows;

        Page<WarningStatusResult> pageObj = PageHelper.startPage(page, listRows);
        List<WarningStatusResult> warnings = warningStatusDao.warningsList(paramJson);


		/* 获取总数 */
        long count = pageObj.getTotal();
		/* 处理报警取消时间 */
        warnings = this.getFinishTime(warnings);

        Map<String, Object> rsMap = new HashMap<>();
        rsMap.put("result", warnings);
        rsMap.put("totalRows", count);
        this.resetWsAmount(paramJson);
        return Result.ok(rsMap).toJSONString();
    }

    /**
     * 更新报警消息
     ***/
    @Override
    public String updateWarning(String jsonStr) {
        /**更新的值**/
        WarningStatusEntity param = JSONObject.parseObject(jsonStr, WarningStatusEntity.class);
        Long id = param.getId();

        Example paramsExample = new Example(WarningStatusEntity.class);
        Example.Criteria paramsCriteria = paramsExample.createCriteria();
        /**查询条件**/
        paramsCriteria.andEqualTo("id", id).andEqualTo("state", 0);
        int exist = warningStatusDao.selectCountByExample(paramsExample);
        if (exist == 0) {
            return Result.error(ResultCode.NODATA_ERR.getCode(), "报警消息不存在或已处理，如有疑问请联系管理员").toJSONString();
        }

        int rs = warningStatusDao.updateByExampleSelective(param, paramsExample);
        if (rs >= 1) {
            return Result.ok(ResultCode.SUCCESS.getCode(), "消息处理成功").toJSONString();
        } else {
            return Result.error(ResultCode.Failed.getCode(), "消息处理失败").toJSONString();
        }
    }

    /**
     *  从websocket获取报警数量
     */
    @Override
    public Map<String, Object> getWsAmount(JSONObject jsonObject) {
        Map<String, Object> warningAmount = RestUtil.getAmount(jsonObject);
        return warningAmount;
    }

    /**
     * 到websocket重置报警数量
     */
    @Override
    public String resetWsAmount(JSONObject jsonObject) {
        if (jsonObject==null||jsonObject.getInteger("amount")==null) {
            jsonObject.put("amount",0);
        }

        Map<String, Object> resetResult = RestUtil.resetAmount(jsonObject);
        return JSONUtils.toJSONString(resetResult);
    }

}