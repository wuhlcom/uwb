/**
 * @author :wuhongliang wuhongliang@zhilutec.com
 * @version :2017年10月24日 下午6:49:05 *
 */
package com.zhilutec.services.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zhilutec.common.utils.*;
import com.zhilutec.db.entities.AreaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.db.daos.PrisonerDao;
import com.zhilutec.db.entities.PrisonerEntity;
import com.zhilutec.db.entities.WarningStatusEntity;
import com.zhilutec.db.results.PrisonerResult;
import com.zhilutec.db.results.WarningStatusResult;
import com.zhilutec.services.IAreaService;
import com.zhilutec.services.IPrisonerCfgService;
import com.zhilutec.services.IPrisonerService;
import com.zhilutec.services.IRedisDataService;
import com.zhilutec.services.IRedisStatusService;
import com.zhilutec.services.ITagWarningService;
import com.zhilutec.services.IWarningStatusService;

import tk.mybatis.mapper.entity.Example;

@Service
public class PrisonerServiceImpl implements IPrisonerService {

    @Autowired
    private PrisonerDao prisonerDao;

    @Resource
    private IAreaService areaService;

    @Resource
    private IRedisStatusService redisStatusService;

    @Resource
    private IPrisonerCfgService prisonerCfgService;

    @Resource
    private ITagWarningService tagWaringService;

    @Resource
    private IWarningStatusService waringStatusService;


    @Resource
    private IRedisDataService redisDataService;

    /*
     * 添加囚犯
     */
    @Override
    public String add(JSONObject jsonObj) {
        PrisonerEntity record = new PrisonerEntity();
        record.setSex(null);

        // 囚徒编码已存在则不添加
        record.setCode(jsonObj.getString("code"));
        record.setIsdel(1);
        int exist = prisonerDao.selectCount(record);
        if (exist >= 1) {
            return Result.error(ResultCode.REPETITION_ERR.getCode(), "囚徒编码已经存在").toJSONString();
        }

        // 身份证已存在则不添加
        String idcard = jsonObj.getString("idcard");
        if (idcard != null && !idcard.isEmpty()) {
            record.setCode(null);
            record.setIdcard(idcard);
            record.setIsdel(1);
            exist = prisonerDao.selectCount(record);
            if (exist >= 1) {
                return Result.error(ResultCode.REPETITION_ERR.getCode(), "身份证已经存在").toJSONString();
            }
        }

        record = JSON.parseObject(jsonObj.toJSONString(), PrisonerEntity.class);
        record.setIsdel(1);
        record.setCreatedAt(System.currentTimeMillis() / 1000);

        // MD5加密
        int rs = prisonerDao.insertSelective(record);
        if (rs >= 1) {
            return Result.ok("添加囚徒成功").toJSONString();
        } else {
            return Result.error(ResultCode.Failed.getCode(), "添加囚徒失败").toJSONString();
        }
    }

    /*
     * 更新囚犯信息
     */
    @Override
    public Result update(JSONObject jsonObj) {
        PrisonerEntity record = new PrisonerEntity();
        record.setSex(null);

        String code = jsonObj.getString("code");
        Long prisonerId = jsonObj.getLong("id");
        if (prisonerId != null) {
            record.setId(prisonerId);
            record.setIsdel(1);
            int exist = prisonerDao.selectCount(record);
            if (exist > 1) {
                return Result.error(ResultCode.NODATA_ERR.getCode(), "囚徒不存在");
            }
        }

        Example paramsExample = new Example(PrisonerEntity.class);
        Example.Criteria paramsCriteria = paramsExample.createCriteria();
        // 设置查询条件 多个andEqualTo串联表示 and条件查询
        if (code != null && !code.trim().isEmpty()) {
            paramsCriteria.andNotEqualTo("id", prisonerId).andEqualTo("isdel", 1).andEqualTo("code", code);
            int exist = prisonerDao.selectCountByExample(paramsExample);
            if (exist >= 1) {
                return Result.error(ResultCode.REPETITION_ERR.getCode(), "此编号已存在");
            }
        }

        String idcard = jsonObj.getString("idcard");
        if (idcard != null && !idcard.trim().isEmpty()) {
            Example paramsExample2 = new Example(PrisonerEntity.class);
            Example.Criteria paramsCriteria2 = paramsExample2.createCriteria();
            paramsCriteria2.andNotEqualTo("id", prisonerId).andEqualTo("isdel", 1).andEqualTo("idcard", idcard);
            int exist = prisonerDao.selectCountByExample(paramsExample2);
            if (exist >= 1) {
                return Result.error(ResultCode.REPETITION_ERR.getCode(), "身份证已经存在");
            }
        }

        record = JSON.parseObject(jsonObj.toJSONString(), PrisonerEntity.class);
        try {
            // 创建example
            Example example = new Example(PrisonerEntity.class);
            // 创建查询条件
            Example.Criteria criteria = example.createCriteria();

            // 设置查询条件 多个andEqualTo串联表示 and条件查询
            if (prisonerId != null) {
                criteria.andEqualTo("id", prisonerId).andEqualTo("isdel", 1);
            } else {
                return Result.error(ResultCode.Failed.getCode(), "更新囚徒时参数错误");
            }

            int rs = prisonerDao.updateByExampleSelective(record, example);
            if (rs >= 1) {
                return Result.ok(ResultCode.SUCCESS.getCode(), "更新囚徒成功");
            } else {
                return Result.error(ResultCode.Failed.getCode(), "更新囚徒失败");
            }
        } catch (Exception e) {
            return Result.error(ResultCode.UNKNOW_ERR.getCode(), "更新囚徒信息出错");
        }
    }

    /*
     * 删除囚犯信息
     */
    @Override
    public Result delete(JSONObject jsonObj) {
        PrisonerEntity record = new PrisonerEntity();
        record.setSex(null);
        String code = jsonObj.getString("code");
        Long prisonerId = jsonObj.getLong("id");

        if (code != null) {
            record.setCode(code);
            record.setIsdel(1);
        } else if (prisonerId != null) {
            record.setId(prisonerId);
            record.setIsdel(1);
        }

        // 创建example
        Example example = new Example(PrisonerEntity.class);
        // 创建查询条件
        Example.Criteria recordCriteria = example.createCriteria();
        // 设置查询条件 多个andEqualTo串联表示 and条件查询
        try {
            if (code != null) {
                recordCriteria.andEqualTo("code", code).andEqualTo("isdel", 0);
            } else if (prisonerId != null) {
                recordCriteria.andEqualTo("id", prisonerId).andEqualTo("isdel", 0);
            }
            int rs = prisonerDao.updateByExampleSelective(record, example);
            if (rs >= 1) {
                return Result.ok(ResultCode.SUCCESS.getCode(), "删除囚徒信息成功");
            } else {
                return Result.error(ResultCode.Failed.getCode(), "删除囚徒信息失败");
            }
        } catch (Exception e) {
            return Result.error(ResultCode.UNKNOW_ERR.getCode(), "删除囚徒信息出错");
        }
    }

    /*
     * 查询囚犯
     */
    @Override
    public PrisonerEntity query(JSONObject jsonObj) {
        String code = jsonObj.getString("code");
        Long tagId = jsonObj.getLong("tagId");
        PrisonerEntity prisoner = new PrisonerEntity();
        prisoner.setSex(null);
        prisoner.setIsdel(1);

        if (code != null && !code.trim().isEmpty()) {
            prisoner.setCode(code);
        } else if (tagId != null) {
            prisoner.setTagId(tagId);
        } else {
            System.out.println("参数错误!");
            return null;
        }

        try {
            prisoner = prisonerDao.selectOne(prisoner);
            prisoner = prisonerNullConvert(prisoner);
            // 计算年龄
            prisoner = countAge(prisoner);
            return prisoner;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * 查询单个囚犯
     */
    @Override
    public PrisonerEntity query(String code) {
        PrisonerEntity prisoner = new PrisonerEntity();
        prisoner.setSex(null);
        prisoner.setIsdel(1);

        if (code != null && !code.trim().isEmpty()) {
            prisoner.setCode(code);
        } else {
            System.out.println("参数错误!");
            return null;
        }
        try {
            prisoner = prisonerDao.selectOne(prisoner);
            prisoner = prisonerNullConvert(prisoner);
            // 计算年龄
            prisoner = countAge(prisoner);
            return prisoner;
        } catch (Exception e) {
            return null;
        }
    }


    /*
     * 查询单个囚犯
     */
    @Override
    public PrisonerEntity queryByTagId(Long tagId) {
        PrisonerEntity prisoner = new PrisonerEntity();
        prisoner.setSex(null);
        prisoner.setIsdel(1);

        if (tagId != null) {
            prisoner.setTagId(tagId);
        } else {
            System.out.println("参数错误!");
            return null;
        }

        try {
            prisoner = prisonerDao.selectOne(prisoner);
            prisoner = prisonerNullConvert(prisoner);
            // 计算年龄
            prisoner = countAge(prisoner);
            return prisoner;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 处理null
     */
    private PrisonerEntity prisonerNullConvert(PrisonerEntity prisoner) {
        if (prisoner == null) {
            return null;
        }
        if (prisoner.getName() == null || prisoner.getName().isEmpty()) {
            prisoner.setName("");
        }

        if (prisoner.getNativePlace() == null || prisoner.getNativePlace().isEmpty()) {
            prisoner.setNativePlace("");
        }

        if (prisoner.getCondemnation() == null || prisoner.getCondemnation().isEmpty()) {
            prisoner.setCondemnation("");
        }

        if (prisoner.getImprisonment() == null || prisoner.getImprisonment().isEmpty()) {
            prisoner.setImprisonment("");
        }

        if (prisoner.getPortrait() == null || prisoner.getPortrait().isEmpty()) {
            prisoner.setPortrait("");
        }
        return prisoner;
    }

    /**
     * 计算年龄
     */
    private PrisonerEntity countAge(PrisonerEntity prisoner) {
        Date birth = prisoner.getBirth();
        Integer age = 0;
        if (birth != null) {
            age = AgeUtil.getAgeFromBirthDate(birth);
        }
        prisoner.setAge(age);
        return prisoner;
    }

    /***计算年龄**/
    private PrisonerResult countAge(PrisonerResult prisoner) {
        Date birth = prisoner.getBirth();
        if (birth != null) {
            int age = AgeUtil.getAgeFromBirthDate(birth);
            prisoner.setAge(age);
        }
        return prisoner;
    }

    /**
     * 查询单个 囚犯信息
     **/
    @Override
    public PrisonerEntity getPrisonerByCode(String code) {
        PrisonerEntity prisoner = prisonerDao.queryByCode(code);
        prisoner = countAge(prisoner);
        prisoner = this.prisonerNullConvert(prisoner);
        return prisoner;
    }

    /***处理囚犯信息**/
    @Override
    public String qureyResult(String code) {
        PrisonerEntity prisoner = this.getPrisonerByCode(code);
        if (prisoner == null) {
            return Result.error(ResultCode.NODATA_ERR.getCode(), "未找到囚犯信息").toJSONString();
        } else {
            return Result.ok(ResultCode.SUCCESS.getCode(), prisoner).toJSONString();
        }
    }

    /***处理囚犯信息**/
    @Override
    public String qureyResult(Long tagId) {
        PrisonerEntity prisoner = this.queryByTagId(tagId);
        if (prisoner == null) {
            return Result.error(ResultCode.NODATA_ERR.getCode(), "未找到囚犯信息").toJSONString();
        } else {
            return Result.ok(ResultCode.SUCCESS.getCode(), prisoner).toJSONString();
        }
    }

    /**
     * 查询一个监仓的所有囚犯信息
     */
    @Override
    public List<PrisonerEntity> queryByArea(String areaCode) {
        Example example = new Example(PrisonerEntity.class);
        Example.Criteria recordCriteria = example.createCriteria();
        recordCriteria.andEqualTo("isdel", 1).andEqualTo("areaCode", areaCode);
        return prisonerDao.selectByExample(example);
    }

    /**
     * 查询一个监仓的所有囚犯信息
     */
    @Override
    public String queryByAreaResult(String jsonStr) {
        Map<String,Object> rsMap= new HashMap<>();
        JSONObject  jsonParam= JSON.parseObject(jsonStr);
        String areaCode = jsonParam.getString("areaCode");
        AreaEntity area = areaService.queryArea(areaCode);
        String areaName=area.getName();
        List<PrisonerEntity> prisoners = this.getPrisoners(areaCode);
        rsMap.put("areaCode",areaCode);
        rsMap.put("areaName",areaName);
        rsMap.put("prisoners",prisoners);
        return Result.ok(rsMap).toJSONString();
    }


    @Override
    public  List<PrisonerEntity> getPrisoners(String areaCode){
        List<PrisonerEntity> prisoners = this.queryByArea(areaCode);
        if (!prisoners.isEmpty()) {
            prisoners = this.dealPrisonerInfo(prisoners,true);
        }
        return prisoners;
    }

    /**
     * 查询一个监仓的所有囚犯信息
     */
    @Override
    public List<PrisonerEntity> queryByAreaList(String jsonStr) {
        JSONObject  jsonParam= JSON.parseObject(jsonStr);
        String areaCode = jsonParam.getString("areaCode");
        List<PrisonerEntity> prisoners = this.queryByArea(areaCode);
        if (!prisoners.isEmpty()) {
            prisoners = dealPrisonerInfo(prisoners,false);
        }
        return prisoners;
    }

    /**
     * 处理返回结果,flag true表示要处理flag值
     **/
    private List<PrisonerEntity> dealPrisonerInfo(List<PrisonerEntity> prisoners,boolean flag) {
        for (PrisonerEntity prisoner : prisoners) {
            prisoner.setAge(null);
            prisoner.setBirth(null);
            prisoner.setCondemnation(null);
            prisoner.setCreatedAt(null);
            prisoner.setAreaCode(null);
            prisoner.setId(null);
            prisoner.setIdcard(null);
            prisoner.setImprisonment(null);
            prisoner.setInTime(null);
            prisoner.setIsdel(null);
            prisoner.setNativePlace(null);
            prisoner.setRemark(null);
            prisoner.setSex(null);
            if (prisoner.getPortrait() == null || prisoner.getPortrait().isEmpty()) {
                prisoner.setPortrait("");
            }
            if (flag) {
                prisoner.setMsg(WarningUtil.OFFLINE);
                prisoner.setFlag(StatusFlagUtil.OFFLINE);
            }
        }
        return prisoners;
    }

    /**
     * 位置人数统计
     */
    public Map<String, Integer> getPositionStatistics(String areaCode) {
        List<PrisonerEntity> prisoners = queryByArea(areaCode);
        HashMap<String, Integer> rsMap = new HashMap<>();
        rsMap.put(PositionUtil.AREA1, 0);
        rsMap.put(PositionUtil.AREA2, 0);
        rsMap.put(PositionUtil.AREA3, 0);
        rsMap.put(PositionUtil.AREA4, 0);
        rsMap.put(PositionUtil.AREA5, 0);
        rsMap.put(PositionUtil.AREA6, 0);
        rsMap.put(PositionUtil.AREA7, 0);
        rsMap.put(PositionUtil.AREA8, 0);
        int area1 = 0;
        int area2 = 0;
        int area3 = 0;
        int area4 = 0;
        int area5 = 0;
        int area6 = 0;
        int area7 = 0;
        int area8 = 0;
        for (PrisonerEntity prisoner : prisoners) {
            WarningStatusEntity data = redisDataService.getRedisData(prisoner.getTagId().toString());
            if (data == null) {
                continue;
            }
            String posCode = data.getPosCode();
            if (posCode == null) {
                continue;
            }
            String code = posCode.substring(posCode.length() - 4);
            System.out.println(code);
            boolean flag = PositionUtil.positionMap.containsKey(code);
            String value = PositionUtil.positionMap.get(code);
            System.out.println(value);
            if (flag && value.equals(PositionUtil.AREA1)) {
                area1++;
            } else if (flag && value.equals(PositionUtil.AREA2)) {
                area2++;
            } else if (flag && value.equals(PositionUtil.AREA3)) {
                area3++;
            } else if (flag && value.equals(PositionUtil.AREA4)) {
                area4++;
            } else if (flag && value.equals(PositionUtil.AREA5)) {
                area5++;
            } else if (flag && value.equals(PositionUtil.AREA6)) {
                area6++;
            } else if (flag && value.equals(PositionUtil.AREA7)) {
                area7++;
            } else if (flag && value.equals(PositionUtil.AREA8)) {
                area8++;
            }
        }

        rsMap.put(PositionUtil.AREA1, area1);
        rsMap.put(PositionUtil.AREA2, area2);
        rsMap.put(PositionUtil.AREA3, area3);
        rsMap.put(PositionUtil.AREA4, area4);
        rsMap.put(PositionUtil.AREA5, area5);
        rsMap.put(PositionUtil.AREA6, area6);
        rsMap.put(PositionUtil.AREA7, area7);
        rsMap.put(PositionUtil.AREA8, area8);
        return rsMap;
    }

    /**
     * 统计单个监仓人数
     */
    @Override
    public Integer countArea(String areaCode) {
        // JSONObject paramObj = JSON.parseObject(jsonStr);
        Example example = new Example(PrisonerEntity.class);
        Example.Criteria recordCriteria = example.createCriteria();
        recordCriteria.andEqualTo("isdel", 1).andEqualTo("areaCode", areaCode);
        return prisonerDao.selectCountByExample(example);
    }

    /*
     * 监仓考勤统计，总人数，实到人数，缺勤人数*****
     *
     * 缺勤, 离线，电子围栏，串仓，配置任一存在，就认为缺勤****
     *
     */
    @Override
    public Map<String, Integer> checkingIn(String areaCode) {
        int absence = 0;
        Map<String, Integer> resultMap = new HashMap<>();
        List<PrisonerEntity> prisoners = this.queryByArea(areaCode);
        // 计算缺勤人数
        for (PrisonerEntity prisoner : prisoners) {
            String code = prisoner.getCode();
            Long tagId = prisoner.getTagId();
            boolean absenceStatus = redisStatusService.absenceStatus(code, tagId);
            if (absenceStatus) {
                absence += 1;
            }
        }

        int total = this.countArea(areaCode);
        int attendances = total - absence;
        resultMap.put("total", total);
        resultMap.put("attendances", attendances);
        resultMap.put("absence", absence);
        return resultMap;
    }

    /**
     * 囚犯信息总接口***
     * 囚犯信息********
     * 囚犯报警列表
     * 囚犯实时坐标从websocket如果websocket异常则给出默认值
     **/
    @Override
    public String getPrisonerInfo(String jsonStr) {
        JSONObject paramJson = JSON.parseObject(jsonStr);
        String code = paramJson.getString("code");
        Integer limit = paramJson.getInteger("limit");
        if (limit == null || limit == 0) {
            limit = 10;
        }
        paramJson.put("limit", limit);

        PrisonerEntity prisonerEntity = this.getPrisonerByCode(code);
        List<WarningStatusResult> prisonerWarnings = waringStatusService.getPrisonerWarnings(jsonStr);
        int count = waringStatusService.countPrisonerWarnings(paramJson);
        Map point =this.getWsPrisoner(paramJson,prisonerEntity);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> rsMap = new HashMap<>();

        resultMap.put("info", prisonerEntity);
        resultMap.put("point",point);
        resultMap.put("warnings", prisonerWarnings);

        rsMap.put("totalRows", count);
        rsMap.put("result", resultMap);
        return Result.ok(rsMap).toJSONString();
    }

    /***从websockeet获取当前囚犯的状态，如果获取失败设置为离线*/
    private Map getWsPrisoner(JSONObject jsonObject,PrisonerEntity prisonerEntity){
        Map prisoner=new HashMap();
        jsonObject.put("tagId",prisonerEntity.getTagId());
        Map<String,Object> rs = RestUtil.getPrisoner(jsonObject);
        Integer errcode = (Integer) rs.get("errcode");
        if (errcode==ResultCode.SUCCESS.getCode()){
            prisoner = (Map)rs.get("result");
        }else {
            prisoner.put("code",prisonerEntity.getCode());
            prisoner.put("tagId",prisonerEntity.getTagId());
            prisoner.put("name",prisonerEntity.getName());
            prisoner.put("flag",StatusFlagUtil.OFFLINE);
        }
       return prisoner;
    }

    /***电子点名信息***/
    @Override
    public String getAttendanceStatistics(String jsonStr) {
        JSONObject paramJson = JSON.parseObject(jsonStr);
        String areaCode = paramJson.getString("areaCode");
        Map<String, Integer> attendances = this.checkingIn(areaCode);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> rsMap = new HashMap<>();
        resultMap.put("attendances", attendances);
        rsMap.put("result", resultMap);
        return Result.ok(rsMap).toJSONString();
    }

}