package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.common.utils.FenceUtil;
import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.dbs.daos.FenceDao;
import com.zhilutec.dbs.entities.Fence;
import com.zhilutec.dbs.entities.Person;
import com.zhilutec.dbs.entities.Strategy;
import com.zhilutec.dbs.pojos.FenceRs;
import com.zhilutec.services.IFenceService;
import com.zhilutec.services.IRedisService;
import com.zhilutec.services.IStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

@Service
public class FenceServiceImpl implements IFenceService {

    @Autowired
    FenceDao fenceDao;

    @Resource
    IStrategyService strategyService;

    @Resource
    IRedisService redisService;


    @Override
    public void fenceCacheInit() {
        Example example = new Example(Fence.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isdel", 1);
        List<Fence> fences = fenceDao.selectByExample(example);
        for (Fence fence : fences) {
            // String fenceCode = fence.getFenceCode();
            // //将区域添加到缓存中
            // String key = ConstantUtil.FENCE_KEY_PRE + ":" + fenceCode;
            // this.put(key, fenceCode, fence, ConstantUtil.REDIS_DEFAULT_TTL);
            this.addFenceCache(fence);
        }
    }

    /**
     * 查询所有区域
     */
    @Override
    public String getfences(JSONObject jsonObject) {

        Map<String, Object> rsMap = new HashMap<>();
        Fence fenceParam = JSONObject.parseObject(jsonObject.toJSONString(), Fence.class);
        Integer page = fenceParam.getPage();
        Integer listRows = fenceParam.getListRows();
        String order = fenceParam.getOrder();
        if (order == null || order.isEmpty()) {
            order = "desc";
            fenceParam.setOrder(order);
        }

        //初始化分页参数
        page = page == null ? 1 : page;
        listRows = listRows == null ? 0 : listRows;

        //进行分页
        Page<Person> pageObj = PageHelper.startPage(page, listRows);
        List<Fence> fences = fenceDao.getFences(fenceParam);

        //获取总数
        long count = pageObj.getTotal();
        if (fences == null && fences.size() == 0) {
            return Result.error("无法查询区域信息").toJSONString();
        }

        rsMap.put("result", fences);
        rsMap.put("totalRows", count);
        return Result.ok(rsMap).toJSONString();
    }

    /**
     * 新增区域
     */
    @Override
    public String add(JSONObject jsonObject) {
        Integer flag = 0;
        Fence record = new Fence();

        String fenceName = jsonObject.getString("fenceName");
        Example example = new Example(Fence.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("fenceName", fenceName).andEqualTo("isdel", 1);
        flag = fenceDao.selectCountByExample(example);

        //区域不存在才能添加
        if (flag == 0) {
            record = JSON.parseObject(jsonObject.toJSONString(), Fence.class);
            Long createdAt = System.currentTimeMillis() / 1000;
            record.setCreatedAt(createdAt);

            String uuid = UUID.randomUUID().toString();
            record.setFenceCode(uuid);
            flag = fenceDao.insertSelective(record);
            if (flag == 0) {
                return Result.error("添加区域失败").toJSONString();
            } else {
                //将区域添加到缓存中
                // String key = ConstantUtil.FENCE_KEY_PRE + ":" + uuid;
                // this.put(key, uuid, record, ConstantUtil.POLICY_TIMEOUT);
                this.addFenceCache(record);
                return Result.ok(ResultCode.SUCCESS).toJSONString();
            }
        } else {
            return Result.error(ResultCode.REPETITION_ERR).toJSONString();
        }
    }

    //更新区域
    //区域有策略且策略使用的情况下不允许修改区域
    //更新区域但没有修改区域坐标参数时不更新缓存
    @Transactional
    @Override
    public String update(JSONObject jsonObject) {
        String fenceCode = jsonObject.getString("fenceCode");
        List<Strategy> strategies = strategyService.getStrategiesByFenceCode(fenceCode, 1);
        if (strategies != null && !strategies.isEmpty()) {
            return Result.error("区域已被策略绑定不允许修改").toJSONString();
        }

        Fence fence = JSONObject.parseObject(jsonObject.toJSONString(), Fence.class);

        Example paramsExample = new Example(Fence.class);
        Example.Criteria paramsCriteria = paramsExample.createCriteria();
        // 设置查询条件 多个andEqualTo串联表示 and条件查询
        paramsCriteria.andEqualTo("fenceCode", fenceCode).andEqualTo("isdel", 1);
        int updateRs = fenceDao.updateByExampleSelective(fence, paramsExample);
        if (updateRs == 1) {
            return Result.ok("修改区域信息成功").toJSONString();
        } else {
            return Result.error("修改区域信息失败").toJSONString();
        }
    }

    /**
     * 根据区域类型分组返回区域
     */
    @Override
    public String groupFencesByType(JSONObject jsonObject) {
        Integer policyType = jsonObject.getInteger("policyType");
        if (policyType == null) {
            policyType = 0;
        }
        List<Object> fenceList = new ArrayList<>();


        Integer type = jsonObject.getInteger("type");
        List<FenceRs> fenceRsList = fenceDao.getFencesByType(type);
        System.out.println(fenceRsList);
        if (fenceRsList != null && fenceRsList.size() > 0) {
            for (FenceRs fenceRs : fenceRsList) {
                Map<String, List<Fence>> fenceMap = new HashMap<>();
                Integer ftype = fenceRs.getType();
                String fTypeName = FenceUtil.FENCE_TYPE.get(ftype);
                List<Fence> fences = fenceRs.getFences();
                System.out.println(fences);
                List<Fence> newFences = new ArrayList<>();
                for (Fence fence : fences) {
                    if (policyType == 1) {
                        //绑定策略已启用
                        List<Strategy> strategies = strategyService.getStrategiesByFenceCode(fence.getFenceCode(), 1);
                        if (strategies != null && !strategies.isEmpty()) {
                            newFences.add(fence);
                        }
                    } else if (policyType == 2) {
                        //绑定策略未启用
                        List<Strategy> strategies = strategyService.getStrategiesByFenceCode(fence.getFenceCode(), 0);
                        if (strategies != null && !strategies.isEmpty()) {
                            newFences.add(fence);
                        }
                    } else if (policyType == 3) {
                        //未绑定策略
                        List<Strategy> strategies = strategyService.getStrategiesByFenceCode(fence.getFenceCode(), null);
                        if (strategies == null || strategies.isEmpty()) {
                            newFences.add(fence);
                        }
                    }
                }
                if (policyType == 0) {
                    newFences = fences;
                }
                fenceMap.put(fTypeName, newFences);
                fenceList.add(fenceMap);
            }
        }
        return Result.ok(ResultCode.SUCCESS.getCode(), fenceList).toJSONString();
    }


    /**
     * 删除区域，区域有策略,不能删除区域
     */
    @Override
    public String delete(JSONObject jsonObject) {
        List fenceCodes = jsonObject.getJSONArray("fenceCodes");

        //查询区域对应的策略是否存在
        List<Strategy> strategies = strategyService.getStrategyByFenceCodes(fenceCodes);
        if (strategies != null && strategies.size() > 0) {
            List<String> existedFences = new ArrayList<>();
            for (Strategy strategy : strategies) {
                existedFences.add(strategy.getFenceCode());
            }
            if (existedFences != null && existedFences.size() > 0) {
                //查询策略对应的区域列表
                List<Fence> fences = this.getFencesByCode(existedFences);
                if (fences != null && fences.size() > 0) {
                    List fenceNames = new ArrayList();
                    for (Fence fence : fences) {
                        fenceNames.add(fence.getFenceName());
                    }
                    String fenceNamesStr = String.join(",", fenceNames);
                    return Result.error("区域:" + "\"" + fenceNamesStr + "\"" + "已绑定策略不允许删除").toJSONString();
                } else {
                    return deleleteFences(fenceCodes);
                }
            } else {
                return deleleteFences(fenceCodes);
            }
        } else {
            return deleleteFences(fenceCodes);
        }

    }

    //删除区域
    private String deleleteFences(List fenceCodes) {
        Example example = new Example(Fence.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("fenceCode", fenceCodes).andEqualTo("isdel", 1);
        Fence fence = new Fence();
        fence.setIsdel(0);
        int rs = fenceDao.updateByExampleSelective(fence, example);
        if (rs > 0) {
            //删除区域缓存
            for (Object fenceCode : fenceCodes) {
                deleteFenceCache(fenceCode.toString());
            }
            return Result.ok("删除区域成功").toJSONString();
        } else {
            return Result.ok("区域已删除").toJSONString();
        }
    }

    @Override
    public Fence getFenceByCode(String code) {
        Fence fence = new Fence();
        fence.setFenceCode(code);
        fence.setIsdel(1);
        return fenceDao.selectOne(fence);
    }

    private List<Fence> getFencesByCode(List<String> fenceCodes) {
        Example example = new Example(Fence.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("fenceCode", fenceCodes).andEqualTo("isdel", 1);
        return fenceDao.selectByExample(example);
    }


    private void addFenceCache(Fence fence) {
        String fenceCode = fence.getFenceCode();
        //将区域添加到缓存中
        String keyPre = ConstantUtil.FENCE_KEY_PRE;
        String key = redisService.genRedisKey(keyPre, fenceCode);
        String str = JSON.toJSONString(fence);
        Map map = JSONObject.parseObject(str, Map.class);
        redisService.hashAddMap(key, map, ConstantUtil.REDIS_DEFAULT_TTL);
    }

    private void deleteFenceCache(String fenceCode) {
        String key = redisService.genRedisKey(ConstantUtil.DEPARTMENT_KEY_PRE, fenceCode);
        redisService.delete(key);
    }
}
