package com.zhilutec.services.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.common.utils.ZlTimeUtil;
import com.zhilutec.common.validators.WarningValidator;

import com.zhilutec.dbs.daos.WarningDao;
import com.zhilutec.dbs.entities.Department;
import com.zhilutec.dbs.entities.Person;
import com.zhilutec.dbs.entities.Strategy;
import com.zhilutec.dbs.entities.Warning;
import com.zhilutec.dbs.pojos.PersonListRs;
import com.zhilutec.dbs.pojos.WarningLevelRs;
import com.zhilutec.dbs.pojos.WarningRs;
import com.zhilutec.services.*;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

@Service
public class WarningServiceImpl extends IRedisService<Warning> implements IWarningService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    WarningDao warningDao;

    @Resource
    IPersonService personService;

    @Resource
    IDepartmentService departmentService;

    @Override
    public List<Warning> getRedisWarnings(String key) {
        return this.getAll(key);
    }

    /**
     * @param key   tagId+strategyCode
     * @param field level
     */
    @Override
    public Warning getReidsWarning(String key, String field) {
        return this.get(key, field);
    }


    @Override
    public void putRedisWarning(String key, String field, Warning warning, long expire) {
        this.put(key, field, warning, expire);
    }

    @Override
    public List<Warning> getRedisWarningList(String keyPre, Long tagId) {
        String warningKey = keyPre + ":" + Long.toString(tagId);
        return this.listAll(warningKey);
    }

    @Override
    public List<String> getRedisStrList(String keyPre, Long tagId) {
        String warningKey = keyPre + ":" + Long.toString(tagId);
        return this.listStr(warningKey);
    }


    @Override
    protected String getRedisKey() {
        return null;
    }

    //删除某个tag下的所有报警
    @Override
    public void delRedisByKey(String keyPre, Long tagId) {
        String warningKey = keyPre + ":" + Long.toString(tagId);
        this.delete(warningKey);
    }

    @Override
    public String warnings(JSONObject jsonObject) {
        Map<String, Object> resultMap = new HashMap<>();
        Warning param = JSONObject.parseObject(jsonObject.toJSONString(), Warning.class);

        Integer page = param.getPage();
        Integer listRows = param.getListRows();
        String order = param.getOrder();
        if (order == null || order.isEmpty()) {
            order = "desc";
            param.setOrder(order);
        }

        //初始化分页参数
        page = page == null ? 1 : page;
        listRows = listRows == null ? 0 : listRows;
        //进行分页
        Page<PersonListRs> pageObj = PageHelper.startPage(page, listRows);
        List<Warning> warnings = warningDao.getWarnings(param);
        //获取总数
        long count = pageObj.getTotal();
        warnings = this.setFinishTime(warnings);
        resultMap.put("result", warnings);
        resultMap.put("totalRows", count);
        return Result.ok(resultMap).toJSONString();
    }

    /**
     * 获取报警的自动取消时间
     */
    private List<Warning> setFinishTime(List<Warning> warnings) {
        RowBounds rowBounds = new RowBounds(0, 1);
        for (Warning warning : warnings) {
            Example example = new Example(Warning.class);
            Example.Criteria criteria = example.createCriteria();
            Long timestamp = warning.getTimestamp();
            Integer level = warning.getLevel();
            Integer type = warning.getType();
            String fenceName = warning.getFenceName();
            String msg = warning.getMsg();

            List<Warning> cancelRecord = null;
            criteria.andEqualTo("op", ConstantUtil.ALARM_OFF)
                    .andEqualTo("level", level)
                    .andEqualTo("type", type)
                    .andEqualTo("fenceName", fenceName)
                    .andEqualTo("msg", msg)
                    .andGreaterThan("timestamp", timestamp);
            example.orderBy("timestamp desc");
            cancelRecord = warningDao.selectByExampleAndRowBounds(example, rowBounds);
            if (cancelRecord != null && !cancelRecord.isEmpty()) {
                warning.setFinishTime(cancelRecord.get(0).getTimestamp());
            } else {
                warning.setFinishTime(0L);
            }
        }
        return warnings;
    }

    private Map<String, Long> dayWarnings(JSONObject paramJson) throws Exception {
        Long startTime = paramJson.getLong("startTime");
        Long endTime = paramJson.getLong("endTime");
        Map<String, Long> param = new HashMap<>();
        if (startTime == null) {
            startTime = ZlTimeUtil.specStamp();
            param.put("startTime", startTime);
        }
        if (endTime == null) {
            endTime = System.currentTimeMillis() / 1000;
            param.put("endTime", endTime);
        }
        List<Map<String, Integer>> warnings = warningDao.warningStatistics(param);
        Map<String, Long> rsMap = getStringIntegerMap(warnings);
        return rsMap;
    }


    private Map<String, Long> monthWarnings() throws Exception {
        Map<String, Long> currMonth = ZlTimeUtil.seMonth();
        List<Map<String, Integer>> warnings = warningDao.warningStatistics(currMonth);
        Map<String, Long> rsMap = getStringIntegerMap(warnings);
        return rsMap;
    }

    private Map<String, Long> getStringIntegerMap(List<Map<String, Integer>> warnings) {
        Map<String, Long> rsMap = new HashMap<>();
        rsMap.put("tip", 0L);
        rsMap.put("common", 0L);
        rsMap.put("urgency", 0L);
        if (warnings != null && warnings.size() > 0) {
            for (Map<String, Integer> warning : warnings) {
                if (warning.get("level") != null) {
                    Long level = Long.parseLong(String.valueOf(warning.get("level")));
                    Long count = Long.parseLong(String.valueOf(warning.get("count")));
                    if (level == 0) {
                        rsMap.put("tip", count);
                    } else if (level == 1) {
                        rsMap.put("common", count);
                    } else if (level == 2) {
                        rsMap.put("urgency", count);
                    }
                }
            }
        }
        return rsMap;
    }

    /**
     * 报警级别leve 0 提示 1 普通 2 严重*
     * 对应的关键字  tip,common,urgency
     */
    private List<WarningRs> getTopWarnings(JSONObject jsonObject) {
        Integer limit = jsonObject.getInteger("limit");
        limit = limit == null ? 20 : limit;
        List<WarningRs> warningRs = warningDao.topPersons(limit);
        if (warningRs != null && warningRs.size() > 0) {
            List<String> personCodes = new ArrayList<>();
            for (WarningRs warning : warningRs) {
                personCodes.add(warning.getPersonCode());
                jsonObject.put("personCodes", personCodes);
            }
            List<WarningLevelRs> warningLevelRs = warningDao.topWarnings(jsonObject);
            if (warningLevelRs != null && warningLevelRs.size() > 0) {
                for (WarningRs warning : warningRs) {
                    String personCode = warning.getPersonCode();
                    for (WarningLevelRs warningLevelR : warningLevelRs) {
                        if (personCode.equals(warningLevelR.getPersonCode())) {
                            Integer level = warningLevelR.getLevel();
                            if (level != null) {
                                if (level == 0) {
                                    Integer count = warningLevelR.getCount();
                                    warning.setTip(count);
                                } else if (level == 1) {
                                    Integer count = warningLevelR.getCount();
                                    warning.setCommon(count);
                                } else if (level == 2) {
                                    Integer count = warningLevelR.getCount();
                                    warning.setUrgency(count);
                                }
                            }
                        }
                    }
                }
            }
        }
        return warningRs;
    }

    /**
     * 按天统计，按月统计，按报警数量显示
     */
    @Transactional
    @Override
    public String getStatistics(JSONObject jsonObject) throws Exception {
        Result rs = WarningValidator.statisticsVal(jsonObject);
        if ((Integer) rs.get("errcode") != ResultCode.SUCCESS.getCode())
            return rs.toJSONString();

        Map<String, Object> rsMap = new HashMap<>();
        Map<String, Long> day = this.dayWarnings(jsonObject);
        Map<String, Long> month = this.monthWarnings();
        List<WarningRs> top = this.getTopWarnings(jsonObject);
        rsMap.put("day", day);
        rsMap.put("month", month);
        rsMap.put("top", top);
        return Result.ok(rsMap).toJSONString();
    }

    @Override
    public String update(JSONObject jsonObject) {
        Warning warning = new Warning();
        Long id = jsonObject.getLong("id");
        Integer status = jsonObject.getInteger("status");
        String remark = jsonObject.getString("remark");
        warning.setStatus(status);
        warning.setId(id);
        warning.setRemark(remark);

        Example example = new Example(Warning.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", id);
        Integer updateRs = warningDao.updateByExampleSelective(warning, example);
        if (updateRs > 0) {
            return Result.ok("修改成功").toJSONString();
        } else {
            return Result.error("修改失败").toJSONString();
        }
    }

    //删除策略对应的报警,未变化的策略报警不要删除
    //首先取出所有报警,找到匹配的围栏,删除，其它报警要保留
    //区分组策略和单用户策略
    @Override
    public void deleteWarningCache(Strategy strategyOld) {
        String uid = strategyOld.getStrategyUserId();
        String strategyCode = strategyOld.getStrategyCode();
        Integer userType = strategyOld.getUserType();
        if (userType == 0) {
            Person person = personService.getPersonById(Long.valueOf(uid));
            Long tagId = person.getTagId();
            if (tagId != null) {
                this.deleteRedisWarning(strategyCode, tagId, ConstantUtil.FENCE_ALARM_KEY_PRE);
            }
        } else if (userType == 1) {
            Department department = departmentService.getDepartmentByCode(uid);
            //获取部门及子部门下的人员
            List<Person> persons = personService.getPersonsByDeparments(department.getDepartmentCode());
            if (persons != null && persons.size() > 0) {
                for (Person person : persons) {
                    Long tagId = person.getTagId();
                    if (tagId != null) {
                        this.deleteRedisWarning(strategyCode, tagId, ConstantUtil.FENCE_ALARM_KEY_PRE);
                    }
                }
            }
        }
    }

    @Override
    public void deleteRedisWarningStr(String keyPre, Long tagId, String warningStr) {
        String warningKey = keyPre + ":" + Long.toString(tagId);
        Long rs = this.remove(warningKey, 0L, warningStr);
        if (rs == null) {
            logger.info("删除Redis List中的报警失败");
        }
    }

    private void deleteRedisWarning(String strategyCode, Long tagId, String warningKeyPre) {
        List<String> warnings = this.getRedisStrList(warningKeyPre, tagId);
        for (String redisWarningStr : warnings) {
            JSONObject redisWarning = JSON.parseObject(redisWarningStr);
            Long redisTagId = redisWarning.getLong("tagId");
            String redisCode = redisWarning.getString("strategyCode");
            if (tagId.longValue() == redisTagId.longValue() && strategyCode.equals(redisCode)) {
                this.deleteRedisWarningStr(warningKeyPre, redisTagId, redisWarningStr);
            }
        }
    }


    /**未处理报警数量统计*/
    @Override
    public String getWarningAmount() {
        Map<String, Integer> rsMap = new HashMap<>();
        Example example = new Example(Warning.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", 0).andEqualTo("op", 1);
        int amount = warningDao.selectCountByExample(example);
        rsMap.put("amount", amount);
        return Result.ok(rsMap).toJSONString();
    }


}
