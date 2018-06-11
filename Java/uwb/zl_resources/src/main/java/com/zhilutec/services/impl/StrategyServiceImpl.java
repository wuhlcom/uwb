package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.common.validators.StrategyValidator;
import com.zhilutec.dbs.daos.StrategyDao;
import com.zhilutec.dbs.entities.*;
import com.zhilutec.dbs.pojos.RedisPolicy;
import com.zhilutec.services.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

@Service
public class StrategyServiceImpl extends IRedisService<RedisPolicy> implements IStrategyService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IPersonService personService;

    @Resource
    IFenceService fenceService;

    @Resource
    IDepartmentService departmentService;

    @Autowired
    StrategyDao strategyDao;

    @Resource
    IWarningService warningService;

    @Resource
    IRedisPolicyService redisPolicyService;

    //初始化策略缓存
    @Override
    // @Transactional //此处事务处理会导致后续的查询，修改，删除策略缓存失败
    public void policyCacheInit() {
        Strategy strategyParam = new Strategy();
        strategyParam.setAvailable(1);
        List<Strategy> strategies = strategyDao.getStrategies(strategyParam);

        if (strategies == null || strategies.isEmpty()) {
            logger.warn("=====找不到策略!=========================");
            return;
        }
        //刷新缓存
        for (Strategy strategy : strategies) {
            String timeValue = strategy.getTimeValue();
            List timeValues = new ArrayList();
            if (timeValue != null && !timeValue.isEmpty()) {
                timeValues = Arrays.asList(timeValue.split(","));
                strategy.setTimeValues(timeValues);
            }

            String fenceCode = strategy.getFenceCode();
            Fence fence = fenceService.getFenceByCode(fenceCode);
            if (fence == null) {
                logger.warn("=============找不到策略对应的区域!==================");
                return;
            }
            this.updateRedisCache(strategy, timeValues);
        }
    }

    //添加策略和策略缓存
    @Transactional
    @Override
    // @Cacheable(value = "policyCache", key = "'policyCache:strategy:'+#jsonObject.getString(\"strategyName\")", unless = "#result==null")
    // @Cacheable(value = "policyCache", key = "'policyCache:strategy:'+#jsonObject.getString(\"strategyName\")", unless = "#result==null")
    public String add(JSONObject jsonObject) throws ParseException {
        Result valRs = StrategyValidator.addVal(jsonObject);
        if ((Integer) valRs.get("errcode") != ResultCode.SUCCESS.getCode()) {
            return valRs.toJSONString();
        }

        String fenceCode = jsonObject.getString("fenceCode");
        Fence fence = fenceService.getFenceByCode(fenceCode);
        if (fence == null)
            return Result.error(ResultCode.REPETITION_ERR.getCode(), "策略对应的区域不存在").toJSONString();

        String timeValue = null;
        List timeValues = jsonObject.getJSONArray("timeValues");
        if (timeValues != null && !timeValues.isEmpty()) {
            timeValue = StringUtils.join(timeValues.toArray(), ",");
        }

        //策略参数检查
        String check = this.addPolicyCheck(jsonObject, timeValue);
        if (check != null) return check;

        Strategy strategy = JSONObject.parseObject(jsonObject.toJSONString(), Strategy.class);
        String uuid = UUID.randomUUID().toString();
        strategy.setStrategyCode(uuid);
        strategy.setCreatedAt(System.currentTimeMillis() / 1000);

        if (timeValue != null && !timeValue.isEmpty())
            strategy.setTimeValue(timeValue);

        Integer insert = strategyDao.insertSelective(strategy);
        if (insert > 0) {
            Integer available = strategy.getAvailable();
            if (available == 1) {
                this.updateRedisCache(strategy, timeValues);
            }
            return Result.ok("配置策略成功").toJSONString();
        } else {
            return Result.error("配置策略失败").toJSONString();
        }
    }

    //策略添加时参数检查
    private String addPolicyCheck(JSONObject jsonObject, String timeValue) {
        //判断策略名是否重复
        String strategyName = jsonObject.getString("strategyName");
        Example example = new Example(Strategy.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("strategyName", strategyName).andEqualTo("isdel", 1);
        int exist = strategyDao.selectCountByExample(example);
        if (exist >= 1) {
            return Result.error(ResultCode.REPETITION_ERR.getCode(), "策略名已经存在").toJSONString();
        }

        //判断策略是否重复,策略区域，策略时间，策略用户，策略级别，策略用户类型
        String fenceCode = jsonObject.getString("fenceCode");
        String startTime = jsonObject.getString("startTime");
        String finishTime = jsonObject.getString("finishTime");
        String strategyUserId = jsonObject.getString("strategyUserId");
        Integer level = jsonObject.getInteger("level");
        Integer userType = jsonObject.getInteger("userType");
        Example example1 = new Example(Strategy.class);
        Example.Criteria criteria1 = example1.createCriteria();

        criteria1.andEqualTo("fenceCode", fenceCode)
                .andEqualTo("startTime", startTime)
                .andEqualTo("finishTime", finishTime)
                .andEqualTo("strategyUserId", strategyUserId)
                .andEqualTo("level", level)
                .andEqualTo("userType", userType)
                .andEqualTo("timeValue", timeValue)
                .andEqualTo("isdel", 1);
        exist = strategyDao.selectCountByExample(example1);
        if (exist >= 1) {
            return Result.error(ResultCode.REPETITION_ERR.getCode(), "策略已经存在").toJSONString();
        }
        return null;
    }

    //查询策略
    @Override
    @Transactional
    public String strategies(JSONObject jsonObject) {
        Map<String, Object> rsMap = new HashMap<>();
        Strategy strategyParam = JSONObject.parseObject(jsonObject.toJSONString(), Strategy.class);
        Integer page = strategyParam.getPage();
        Integer listRows = strategyParam.getListRows();
        String order = strategyParam.getOrder();
        if (order == null || order.isEmpty()) {
            order = "desc";
            strategyParam.setOrder(order);
        }

        //初始化分页参数
        page = page == null ? 1 : page;
        listRows = listRows == null ? 0 : listRows;

        //进行分页
        Page<Strategy> pageObj = PageHelper.startPage(page, listRows);
        List<Strategy> strategies = strategyDao.getStrategies(strategyParam);
        //获取总数
        long count = pageObj.getTotal();

        // if (strategies == null || strategies.isEmpty()) {
        //     return Result.error("无法查询策略信息").toJSONString();
        // }


        //处理重复周期和关联的用户
        for (Strategy strategy : strategies) {
            Integer type = strategy.getUserType();
            if (strategy.getRemark() == null) {
                strategy.setRemark("");
            }

            String timeValue = strategy.getTimeValue();
            List timeValues = new ArrayList();
            if (timeValue != null && !timeValue.isEmpty()) {
                timeValues = Arrays.asList(timeValue.split(","));
                strategy.setTimeValues(timeValues);
            }

            //加上策略用户名
            String stratebyUserId = strategy.getStrategyUserId();
            if (type == 0) {
                Person person = personService.getPersonById(Long.valueOf(stratebyUserId));
                strategy.setStrategyUser(person.getPersonName());
            } else if (type == 1) {
                Department department = departmentService.getDepartmentByCode(stratebyUserId);
                //返回父级部门编号供前端使用
                List parentCodes = new ArrayList();
                parentCodes = departmentService.getAllParentCodes(parentCodes, stratebyUserId);
                Collections.reverse(parentCodes);
                strategy.setParentCodes(parentCodes);
                strategy.setStrategyUser(department.getDepartmentName());
            }

            //刷新策略缓存,调试用,后期要关闭以名影响性能
            // Integer available = strategy.getAvailable();
            // if (available == 1) {
            //     String fenceCode = strategy.getFenceCode();
            //     Fence fence = fenceService.getFenceByCode(fenceCode);
            //     if (fence == null)
            //         return Result.error(ResultCode.REPETITION_ERR.getCode(), "策略对应的区域不存在").toJSONString();
            //     this.updateRedisCache(strategy, timeValues);
            // }
        }

        rsMap.put("result", strategies);
        rsMap.put("totalRows", count);
        return Result.ok(rsMap).toJSONString();
    }


    //更新策略缓存
    private void updateRedisCache(Strategy strategy, List timeValues) {
        RedisPolicy redisPolicy = new RedisPolicy();
        redisPolicy.setStrategyUserId(strategy.getStrategyUserId());
        redisPolicy.setStrategyName(strategy.getStrategyName());
        redisPolicy.setStrategyCode(strategy.getStrategyCode());
        redisPolicy.setFenceCode(strategy.getFenceCode());
        redisPolicy.setStartTime(strategy.getStartTime());
        redisPolicy.setFinishTime(strategy.getFinishTime());
        redisPolicy.setForbidden(strategy.getForbidden());
        redisPolicy.setLevel(strategy.getLevel());
        redisPolicy.setTimeValues(timeValues);
        redisPolicy.setTimeType(strategy.getTimeType());
        redisPolicy.setUserType(strategy.getUserType());
        redisPolicy.setAvailable(strategy.getAvailable());
        //添加redis
        this.redisCacheAdd(redisPolicy);
    }


    //获取策略
    private Strategy getStrategyByCode(String code) {
        Strategy strategy = new Strategy();
        strategy.setIsdel(1);
        strategy.setStrategyCode(code);
        return strategyDao.selectOne(strategy);
    }

    //获取已删除策略
    private Strategy getDelStrategyByCode(String code) {
        Strategy strategy = new Strategy();
        strategy.setIsdel(0);
        strategy.setStrategyCode(code);
        return strategyDao.selectOne(strategy);
    }


    /**
     * 开关策略*
     * 关策略要删除策略缓存和报警缓存
     * 开策略要添加策略缓存
     */
    @Override
    @Transactional
    public String policySwitch(JSONObject jsonObject) throws InterruptedException {
        Result valRs = StrategyValidator.swVal(jsonObject);
        if ((Integer) valRs.get("errcode") != ResultCode.SUCCESS.getCode()) {
            return valRs.toJSONString();
        }

        Strategy strategy = new Strategy();
        String strategyCode = jsonObject.getString("strategyCode");
        Integer available = jsonObject.getInteger("available");
        strategy.setStrategyCode(strategyCode);
        strategy.setAvailable(available);

        Strategy strategyOld = this.getStrategyByCode(strategyCode);
        Example paramsExample = new Example(Strategy.class);
        Example.Criteria paramsCriteria = paramsExample.createCriteria();
        // 设置查询条件 多个andEqualTo串联表示 and条件查询
        paramsCriteria.andEqualTo("strategyCode", strategyCode).andEqualTo("isdel", 1);
        int updateRs = strategyDao.updateByExampleSelective(strategy, paramsExample);
        if (updateRs > 0) {
            if (strategy.getAvailable() == 0) {
                //关闭策略
                //先删除策略再删除报警注意顺序
                this.deletePolicyCache(strategyOld);
                warningService.deleteWarningCache(strategyOld);
                return Result.ok("关闭策略成功").toJSONString();
            } else {
                //打开策略
                List<String> timeValues = Arrays.asList(strategyOld.getTimeValue().split(","));
                this.updateRedisCache(strategyOld, timeValues);
                return Result.ok("启用策略成功").toJSONString();
            }
        } else {
            return Result.error("操作策略失败").toJSONString();
        }
    }

    /**
     * 修改策略时，有效区域，报警级别，策略行为，关联用户，时间，重复周期发现变化，
     * 旧的策略缓存要更新，旧策略相关的报警缓存要全部清除
     * 此接口不能修改策略的启用还是禁用状态
     */
    @Override
    @Transactional
    public String update(JSONObject jsonObject) throws ParseException, InterruptedException {
        Result valRs = StrategyValidator.updateVal(jsonObject);
        if ((Integer) valRs.get("errcode") != ResultCode.SUCCESS.getCode()) {
            return valRs.toJSONString();
        }

        String fenceCode = jsonObject.getString("fenceCode");

        Strategy strategy = JSONObject.parseObject(jsonObject.toJSONString(), Strategy.class);
        Integer available = strategy.getAvailable();
        strategy.setAvailable(null);

        List timeValues = jsonObject.getJSONArray("timeValues");
        String timeValue = StringUtils.join(timeValues.toArray(), ",");
        if (timeValues != null && !timeValues.isEmpty()) {
            strategy.setTimeValue(timeValue);
        }

        String strategyCode = strategy.getStrategyCode();
        Fence fence = new Fence();
        //获取旧策略
        Strategy strategyOld = this.getStrategyByCode(strategyCode);

        if (fenceCode != null) {
            fence = fenceService.getFenceByCode(fenceCode);
            if (fence == null)
                return Result.error(ResultCode.REPETITION_ERR.getCode(), "策略对应的区域不存在").toJSONString();
        } else {
            fence = fenceService.getFenceByCode(strategyOld.getFenceCode());
        }
        jsonObject.put("fenceCode", fence.getFenceCode());

        //策略重复检查
        String check = this.updatePolicyCheck(jsonObject);
        if (check != null) return check;

        Example paramsExample = new Example(Strategy.class);
        Example.Criteria paramsCriteria = paramsExample.createCriteria();
        // 设置查询条件 多个andEqualTo串联表示 and条件查询
        paramsCriteria.andEqualTo("strategyCode", strategyCode).andEqualTo("isdel", 1);

        int updateRs = strategyDao.updateByExampleSelective(strategy, paramsExample);
        strategy.setAvailable(available);
        if (updateRs >= 1) {
            //查出更新后的策略
            Strategy strategyUpdate = this.getStrategyByCode(strategyCode);
            Integer availableUpdate = strategyUpdate.getAvailable();
            String updateTimeValue = strategyUpdate.getTimeValue();
            List<String> updateTimeValues = Arrays.asList(updateTimeValue.split(","));
            if (availableUpdate == 1) {
                //刷新策略缓存
                this.deletePolicyCache(strategyOld);
                this.updateRedisCache(strategyUpdate, updateTimeValues);
            } else {
                //删除缓存
                this.deletePolicyCache(strategyOld);
            }

            //处理报警
            this.deleteRedisWarning(jsonObject, strategyOld);
            return Result.ok("修改策略成功").toJSONString();
        } else {
            return Result.error("修改策略失败或策略未修改").toJSONString();
        }
    }

    //更新策略时根据情况，删除报警缓存
    //当策略状态，围栏，开始时间，结束时间，重复周期，行为任一发生变化要修改缓存，策略名变化不修改缓存
    private void deleteRedisWarning(JSONObject jsonObject, Strategy strategyOld) {
        Integer available = jsonObject.getInteger("available");
        String fenceCode = jsonObject.getString("fenceCode");
        String finishTime = jsonObject.getString("finishTime");
        String startTime = jsonObject.getString("startTime");
        String strategyUserId = jsonObject.getString("strategyUserId");
        Integer level = jsonObject.getInteger("level");
        List timeValues = jsonObject.getJSONArray("timeValues");
        Integer forbidden = jsonObject.getInteger("forbidden");

        Integer sAvailable = strategyOld.getAvailable();
        String sFenceCode = strategyOld.getFenceCode();
        String sFinishTime = strategyOld.getFinishTime().toString();
        String sStartTime = strategyOld.getStartTime().toString();
        String sStrategyUserId = strategyOld.getStrategyUserId();
        Integer sLevel = strategyOld.getLevel();
        List sTimeValues = strategyOld.getTimeValues();
        Integer sForbidden = strategyOld.getForbidden();

        boolean isAvailable = available == sAvailable;
        boolean isFenceCode = fenceCode.equals(sFenceCode);
        boolean isFinishTime = finishTime.equals(sFinishTime);
        boolean isStartTime = startTime.equals(sStartTime);
        boolean isStrategyUserId = strategyUserId.equals(sStrategyUserId);
        boolean isLevel = level.equals(sLevel);
        boolean isTimeValues = timeValues.equals(sTimeValues);
        boolean isForbidden = forbidden == sForbidden;

        boolean isChanged = (isAvailable && isFenceCode && isFinishTime && isStartTime && isStrategyUserId && isLevel && isTimeValues && isForbidden);
        //策略关键配置变化要删除旧的报警
        if (!isChanged) {
            warningService.deleteWarningCache(strategyOld);
        }
    }


    //删除策略缓存
    private void deletePolicyCache(Strategy strategyOld) {
        RedisPolicy redisPolicy = new RedisPolicy();
        redisPolicy.setStrategyCode(strategyOld.getStrategyCode());
        redisPolicy.setUserType(strategyOld.getUserType());
        redisPolicy.setStrategyUserId(strategyOld.getStrategyUserId());
        this.redisPolicyDel(redisPolicy);
    }

    //update param check
    private String updatePolicyCheck(JSONObject jsonObject) throws ParseException {
        //判断策略名是否重复
        String strategyName = jsonObject.getString("strategyName");
        String strategyCode = jsonObject.getString("strategyCode");
        // Integer available = jsonObject.getInteger("available");

        //判断修改的策略名与别的策略名是否重复
        Example example = new Example(Strategy.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("strategyCode", strategyCode).andEqualTo("strategyName", strategyName).andEqualTo("isdel", 1);
        int exist = strategyDao.selectCountByExample(example);
        if (exist >= 1) {
            return Result.error(ResultCode.REPETITION_ERR.getCode(), "策略名已经存在").toJSONString();
        }
        return null;
    }

    //删除策略要删除策略对应的策略缓存和报警
    //启用状态的策略不允许删除
    @Transactional
    @Override
    // @CacheEvict(value = "policyCache", key = "'policyCache:strategy:'+#jsonObject.getString(\"strategyName\")")
    public String delete(JSONObject jsonObject) {
        Result valRs = StrategyValidator.delVal(jsonObject);
        if ((Integer) valRs.get("errcode") != ResultCode.SUCCESS.getCode()) {
            return valRs.toJSONString();
        }

        List strategyCodes = jsonObject.getJSONArray("strategyCodes");

        Example example1 = new Example(Strategy.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andIn("strategyCode", strategyCodes).andEqualTo("isdel", 1).andEqualTo("available", 0);
        List<Strategy> strategies = strategyDao.selectByExample(example1);
        if (strategies != null && !strategies.isEmpty()) {
            List<String> delStrategies = new ArrayList<>();
            for (Strategy strategy : strategies) {
                delStrategies.add(strategy.getStrategyCode());
            }

            Example example = new Example(Strategy.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("strategyCode", delStrategies).andEqualTo("isdel", 1);
            Strategy strategy = new Strategy();
            strategy.setIsdel(0);
            int rs = strategyDao.updateByExampleSelective(strategy, example);
            if (rs > 0) {
                for (Object strategyCode : delStrategies) {
                    Strategy strategyOld = this.getDelStrategyByCode(strategyCode.toString());
                    //删除策略缓存
                    this.deletePolicyCache(strategyOld);
                    //删除报警缓存
                    warningService.deleteWarningCache(strategyOld);
                }
                return Result.ok("删除策略成功").toJSONString();
            } else {
                return Result.ok("策略已删除").toJSONString();
            }
        } else {
            return Result.error("策略已启用不允许删除").toJSONString();
        }
    }

    //删除部门时要删除策略
    @Transactional
    @Override
    public void deleteByDptUser(List<String> strategyUserIds) {
        Example example1 = new Example(Strategy.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andIn("strategyUserId", strategyUserIds).andEqualTo("isdel", 1);
        List<Strategy> strategies = strategyDao.selectByExample(example1);

        if (strategies != null && strategies.size() > 0) {

            Example example = new Example(Strategy.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("strategyUserId", strategyUserIds).andEqualTo("isdel", 1);

            Strategy strategy = new Strategy();
            strategy.setIsdel(0);
            //删除策略
            int rs = strategyDao.updateByExampleSelective(strategy, example);

            if (rs > 0) {
                for (Strategy policy : strategies) {
                    //删除策略缓存
                    this.deletePolicyCache(policy);
                    //删除报警缓存
                    warningService.deleteWarningCache(policy);
                }
            }
        }
    }

    //展开策略缓存
    //使用tagId为key,strategyCode作为field
    private void redisCacheAdd(RedisPolicy redisPolicy) {
        Integer userType = redisPolicy.getUserType();
        String userId = redisPolicy.getStrategyUserId();
        String strategyCode = redisPolicy.getStrategyCode();
        if (userType == 0) {
            Person person = personService.getPersonById(Long.valueOf(userId));
            this.addTagidPolicy(person.getTagId(), strategyCode, redisPolicy);
        } else if (userType == 1) {
            //获取部门信息
            Department department = departmentService.getDepartmentByCode(userId);
            if (department == null)
                return;
            //获取部门及子部门下的人员
            List<Person> persons = personService.getPersonsByDeparments(department.getDepartmentCode());
            //当部门下没人员时不添加策略缓存
            if (persons != null && persons.size() > 0) {
                for (Person person : persons) {
                    //人员如果没有配置tagId则不添加策略
                    Long tagId = person.getTagId();
                    if (tagId != null) {
                        this.addTagidPolicy(tagId, strategyCode, redisPolicy);
                    }
                }
            }
        }
    }


    //删除指定策略的策略缓存
    private void redisPolicyDel(RedisPolicy redisPolicy) {
        Integer type = redisPolicy.getUserType();
        String userId = redisPolicy.getStrategyUserId();
        String strategyCode = redisPolicy.getStrategyCode();
        if (type == 0) {
            Person person = personService.getPersonById(Long.valueOf(userId));
            removeTagidPolicy(person.getTagId(), strategyCode);
        } else if (type == 1) {
            //获取部门信息
            Department department = departmentService.getDepartmentByCode(userId);
            //获取部门及子部门下的人员
            List<Person> persons = personService.getPersonsByDeparments(department.getDepartmentCode());
            if (persons != null && persons.size() > 0) {
                for (Person person : persons) {
                    Long tagId = person.getTagId();
                    if (tagId != null) {
                        this.removeTagidPolicy(tagId, strategyCode);
                    }
                }
            }
        }
    }

    //添加单个标签下的策略缓存
    @Override
    public void addTagidPolicy(Long tagId, String strategyCode, RedisPolicy redisPolicy) {
        String key = this.genIdKey(ConstantUtil.POLICY_KEY_PRE, tagId);
        this.put(key, strategyCode, redisPolicy, ConstantUtil.REDIS_DEFAULT_TTL);
        // RedisPolicy redisPolicy1 = this.getReidsPolicy(key,strategyCode);
        // System.out.println(redisPolicy1);
    }

    //删除单个标签下的某个策略缓存,一个标签可能有多个策略缓存
    @Override
    public void removeTagidPolicy(Long tagId, String strategyCode) {
        String key = this.genIdKey(ConstantUtil.POLICY_KEY_PRE, tagId);
        Long del = this.remove(key, strategyCode);
        if (del == null) {
            logger.info("删除TAGID " + tagId + " 策略失败");
        }
    }

    //根据围栏查询策略
    @Override
    public List<Strategy> getStrategyByFenceCodes(List<String> fenceCodes) {
        Example example = new Example(Strategy.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("fenceCode", fenceCodes).andEqualTo("isdel", 1);
        return strategyDao.selectByExample(example);
    }

    @Override
    public List<Strategy> getStrategiesByFenceCode(String fenceCode, Integer available) {
        Example example = new Example(Strategy.class);
        Example.Criteria criteria = example.createCriteria();
        if (available == null) {
            criteria.andEqualTo("fenceCode", fenceCode).andEqualTo("isdel", 1);
        } else {
            criteria.andEqualTo("fenceCode", fenceCode).andEqualTo("isdel", 1).andEqualTo("available", available);
        }
        return strategyDao.selectByExample(example);
    }


    @Override
    public List<Strategy> getStrategyByUserId(String strategyUserId) {
        Example example = new Example(Strategy.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("strategyUserId", strategyUserId).andEqualTo("isdel", 1);
        return strategyDao.selectByExample(example);
    }


    @Override
    public void delRedisByKey(String keyPre, Long tagId) {
        String key = keyPre + ":" + Long.toString(tagId);
        this.delete(key);
    }


    private List<RedisPolicy> getRedisPolicies(Long tagId) {
        String key = ConstantUtil.POLICY_KEY_PRE + ":" + Long.toString(tagId);
        return this.getAll(key);
    }

    private RedisPolicy getReidsPolicy(String strategyKey, String strategyCode) {
        // System.out.println("redis hget 策略key 为 " + strategyKey);
        // System.out.println("redis hget 策略field 为 " + strategyCode);
        return this.get(strategyKey, strategyCode);
    }

    @Override
    protected String getRedisKey() {
        return null;
    }
}
