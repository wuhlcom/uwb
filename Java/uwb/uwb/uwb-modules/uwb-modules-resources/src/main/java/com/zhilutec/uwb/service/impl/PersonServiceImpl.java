package com.zhilutec.uwb.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhilutec.uwb.common.validators.PersonValidator;
import com.zhilutec.uwb.dao.DepartmentDao;
import com.zhilutec.uwb.dao.PersonDao;
import com.zhilutec.uwb.dao.StrategyDao;
import com.zhilutec.uwb.entity.Department;
import com.zhilutec.uwb.entity.Person;
import com.zhilutec.uwb.entity.Strategy;
import com.zhilutec.uwb.common.pojo.DepartmentRs;
import com.zhilutec.uwb.common.pojo.PersonListRs;
import com.zhilutec.uwb.common.pojo.RedisPolicy;
import com.zhilutec.uwb.service.IDepartmentService;
import com.zhilutec.uwb.service.IPersonService;
import com.zhilutec.uwb.service.IRedisService;
import com.zhilutec.uwb.service.IStrategyService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhilutec.uwb.result.Result;
import com.zhilutec.uwb.result.ResultCode;
import tk.mybatis.mapper.entity.Example;
import com.zhilutec.uwb.util.AgeUtil;
import com.zhilutec.uwb.util.ConstantUtil;

import javax.annotation.Resource;
import java.util.*;

@Service
public class PersonServiceImpl implements IPersonService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    PersonDao personDao;

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    StrategyDao strategyDao;

    @Resource
    IDepartmentService departmentService;

    @Resource
    IStrategyService strategyService;

    @Resource
    IRedisService redisService;

    @Override
    @Transactional
    public void personCacheInit() {
        Example example = new Example(Person.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isdel", 1).andIsNotNull("tagId");
        List<Person> people = personDao.selectByExample(example);
        for (Person person : people) {
            Long tagId = person.getTagId();
            this.addPersonCache(tagId, person);
        }
    }


    /**
     * 获取直接部门为顶级部门的人员信息
     */
    @Override
    public String getPersonsInTopDepartment() {
        List<Person> persons = personDao.getPersonsByDepartment(ConstantUtil.TOP_DEPARTMENT_CODE);
        return Result.ok(persons).toJSONString();
    }

    /**
     * 根据部门分组显示人员
     */
    @Override
    public List<DepartmentRs> getPersonsByGroup() {
        return personDao.groupPersonsByDepartment();
    }

    /**
     * 获取人员清单，包含部门，手环信息
     **/
    @Transactional
    @Override
    public String getPersonAndDepartmentRs(JSONObject jsonObject) {
        Map<String, Object> rsMap = new HashMap<>();
        Person personParam = JSONObject.parseObject(jsonObject.toJSONString(), Person.class);
        Integer page = personParam.getPage();
        Integer listRows = personParam.getListRows();
        String order = personParam.getOrder();
        if (order == null || order.isEmpty()) {
            order = "desc";
            personParam.setOrder(order);
        }

        //初始化分页参数
        page = page == null ? 1 : page;
        listRows = listRows == null ? 0 : listRows;

        //进行分页
        Page<PersonListRs> pageObj = PageHelper.startPage(page, listRows);
        List<PersonListRs> persons = personDao.getPersonList(personParam);
        //获取总数
        long count = pageObj.getTotal();

        if (persons != null && !persons.isEmpty()) {
            persons = this.getPersonAndDepartments(persons);
        } else {
            return Result.error("无法查询到人员信息").toJSONString();
        }

        rsMap.put("result", persons);
        rsMap.put("totalRows", count);
        return Result.ok(rsMap).toJSONString();
    }

    /**
     * 获取人员的直接部门后还要添加上级部门
     * 计算年龄
     */
    private List<PersonListRs> getPersonAndDepartments(List<PersonListRs> persons) {
        for (PersonListRs person : persons) {
            this.countAge(person);
            String departmentCode = person.getDepartmentCode();
            List<String> parents = new ArrayList();
            //递归获取上级部门
            parents = departmentService.getAllParents(parents, departmentCode);
            if (!parents.isEmpty() && parents.size() != 1) {
                Collections.reverse(parents);
                String newDepartmentName = StringUtils.join(parents.toArray(), "/");
                person.setDepartmentName(newDepartmentName);
            }
        }
        return persons;
    }

    /**
     * 计算年龄
     */
    private PersonListRs countAge(PersonListRs personListRs) {
        Date birth = personListRs.getBirth();
        Integer age = 0;
        if (birth != null) {
            age = AgeUtil.getAgeFromBirthDate(birth);
        }
        personListRs.setAge(age);
        return personListRs;
    }


    /**
     * 添加人员
     */
    @Transactional
    @Override
    public String add(JSONObject jsonObject) {
        Person person = JSONObject.parseObject(jsonObject.toJSONString(), Person.class);
        String check = this.addParamCheck(jsonObject, person);
        if (check != null) return check;
        Long createdAt = System.currentTimeMillis() / 1000;
        person.setCreatedAt(createdAt);
        int insert = personDao.insertSelective(person);
        if (insert > 0) {
            Long tagId = jsonObject.getLong("tagId");
            this.addPersonCache(tagId, person);
            this.addPolicy(jsonObject);
            return Result.ok("添加人员成功").toJSONString();
        } else {
            return Result.error("添加人员失败").toJSONString();
        }
    }


    /**
     * 添加将部门策略展开到人员缓存
     * 对于围栏策略，一个部门可以有多个围栏，这样就可能会有多个策略
     */
    private void addPolicy(JSONObject jsonObject) {
        Long tagId = jsonObject.getLong("tagId");
        String departmentCode = jsonObject.getString("departmentCode");
        if (tagId != null && departmentCode != null && !departmentCode.isEmpty()) {
            List<Strategy> strategies = strategyService.getStrategyByUserId(departmentCode);
            if (strategies != null && strategies.size() > 0) {
                this.addDptPolicy(tagId, strategies);
            }
        }
    }


    //添加时参数校验
    private String addParamCheck(JSONObject jsonObject, Person person) {
        Person record = new Person();
        //默认会添加到顶级部门
        String departmentCode = jsonObject.getString("departmentCode");
        if (departmentCode == null || departmentCode.isEmpty()) {
            person.setDepartmentCode(ConstantUtil.TOP_DEPARTMENT_CODE);
        }

        // 人员编号已经存在
        String personCode = jsonObject.getString("personCode");
        int exist = 0;
        if (personCode != null && !personCode.isEmpty()) {
            record.setPersonCode(personCode);
            record.setIsdel(1);
            exist = personDao.selectCount(record);
            if (exist >= 1) {
                return Result.error("人员编号已被使用").toJSONString();
            }
        }

        // 邮箱已存在则不添加
        String email = jsonObject.getString("email");
        if (email != null && !email.isEmpty()) {
            record.setPersonCode(null);
            record.setEmail(email);
            record.setIsdel(1);
            exist = personDao.selectCount(record);
            if (exist >= 1) {
                return Result.error("邮箱已经存在").toJSONString();
            }
        }

        // 电话号码已存在则不添加
        String tel = jsonObject.getString("telephone");
        if (tel != null && !tel.isEmpty()) {
            record.setPersonCode(null);
            record.setEmail(null);
            record.setTelephone(tel);
            record.setIsdel(1);
            exist = personDao.selectCount(record);
            if (exist >= 1) {
                return Result.error("电话号码已经存在").toJSONString();
            }
        }

        // 身份证已存在则不添加
        String idcard = jsonObject.getString("idcard");
        if (idcard != null && !idcard.isEmpty()) {
            record.setPersonCode(null);
            record.setEmail(null);
            record.setTelephone(null);
            record.setIdcard(idcard);
            record.setIsdel(1);
            exist = personDao.selectCount(record);
            if (exist >= 1) {
                return Result.error("身份证号码已经存在").toJSONString();
            }
        }

        // tagId被添加
        Long tagId = jsonObject.getLong("tagId");
        if (tagId != null) {
            record.setPersonCode(null);
            record.setEmail(null);
            record.setTelephone(null);
            record.setIdcard(null);
            record.setTagId(tagId);
            record.setIsdel(1);
            exist = personDao.selectCount(record);
            if (exist >= 1) {
                return Result.error("手环ID已被用户绑定").toJSONString();
            }
        }
        return null;
    }

    /**
     * @param jsonObject 必须包含id
     *                   有以下几种情况，更新人员策略缓存操作
     *                   更新人员tagId:tagId从无到有，tagId改变
     *                   不更新tagId:一直tagId为空，tagId不为空但不变化
     */
    @Transactional
    @Override
    public String update(JSONObject jsonObject) throws InterruptedException {
        Result rs = PersonValidator.updateVal(jsonObject);
        if ((Integer) rs.get("errcode") != ResultCode.SUCCESS.getCode()) {
            return rs.toJSONString();
        }

        String check = this.updateParamCheck(jsonObject);
        if (check != null) return check;

        Long id = jsonObject.getLong("id");
        Person person = JSONObject.parseObject(jsonObject.toJSONString(), Person.class);
        person.setIsdel(1);
        Example paramsExample = new Example(Person.class);
        Example.Criteria paramsCriteria = paramsExample.createCriteria();
        // 设置查询条件 多个andEqualTo串联表示 and条件查询
        paramsCriteria.andEqualTo("id", id).andEqualTo("isdel", 1);
        //必须在更新之前查询
        Person personOld = this.getPersonById(id);
        int updateRs = personDao.updateByExample(person, paramsExample);
        if (updateRs == 1) {
            Person personNew = this.getPersonById(id);
            //新的tagId
            Long tagId = jsonObject.getLong("tagId");
            String departmentCode = jsonObject.getString("departmentCode");
            //旧的tagId
            Long oldTagId = personOld.getTagId();
            String oldDptCode = personOld.getDepartmentCode();

            //新tagId为空，旧tagId不为空,删除对应策略和报警缓存
            if (tagId == null && oldTagId != null) {
                this.removePolicyAlarmCache(oldTagId);
                this.delPersonCache(oldTagId);
            } else if (tagId != null && oldTagId != null) {
                //tagId不为空，旧tagId不为空，且新旧tagId不相同
                //当tagId与旧tagId不相同，部门不为空且部门有策略时刷新策略缓存同时要删除旧的策略缓存
                if (tagId.longValue() != oldTagId.longValue()) {
                    this.removePolicyAlarmCache(oldTagId);
                    //删除旧的人员缓存
                    this.delPersonCache(oldTagId);
                    //部门不为空,更新策略,部门Id为空不更新策略
                    if (departmentCode != null && !departmentCode.isEmpty()) {
                        this.updatePersonPolicy(tagId, departmentCode);
                    }
                } else {
                    //tagId不为空，旧tagId不为空，且新旧tagId相同,部门发生变化更新策略缓存
                    if (departmentCode != null && !departmentCode.isEmpty()) {
                        //如果更新部门,则要更新策略
                        if (!departmentCode.equals(oldDptCode)) {
                            //不论是否有缓存都删除一次旧tag的策略，报警，人员缓存
                            this.removePolicyAlarmCache(oldTagId);
                            this.updatePersonPolicy(tagId, departmentCode);
                        }
                    }
                }
            } else if (tagId != null && oldTagId == null) {
                //tagId不为空，旧tagId为空,添加缓存策略
                this.updatePersonPolicy(tagId, departmentCode);
            }

            //tagId存在更新人员缓存
            if (tagId != null) {
                this.addPersonCache(tagId, personNew);
            }
            return Result.ok("修改人员成功").toJSONString();
        } else {
            return Result.error("修改人员失败或未修改").toJSONString();
        }
    }


    //更新参数校验
    private String updateParamCheck(JSONObject jsonObject) {
        int exist = 0;
        Long id = jsonObject.getLong("id");
        String personCode = jsonObject.getString("personCode");
        if (personCode != null && !personCode.isEmpty()) {
            Example paramsExample = new Example(Person.class);
            Example.Criteria paramsCriteria = paramsExample.createCriteria();
            // 设置查询条件 多个andEqualTo串联表示 and条件查询
            paramsCriteria.andNotEqualTo("id", id).andEqualTo("isdel", 1).andEqualTo("personCode", personCode);

            exist = personDao.selectCountByExample(paramsExample);
            if (exist >= 1) {
                return Result.error("人员编号已被使用").toJSONString();
            }
        }

        // 邮箱已存在则不添加
        String email = jsonObject.getString("email");
        if (email != null && !email.isEmpty()) {
            Example paramsExample1 = new Example(Person.class);
            Example.Criteria paramsCriteria1 = paramsExample1.createCriteria();
            // 设置查询条件 多个andEqualTo串联表示 and条件查询
            paramsCriteria1.andNotEqualTo("id", id).andEqualTo("isdel", 1).andEqualTo("email", email);
            exist = personDao.selectCountByExample(paramsExample1);
            if (exist >= 1) {
                return Result.error("邮箱已被使用").toJSONString();
            }
        }

        // 电话号码已存在则不添加
        String telephone = jsonObject.getString("telephone");
        if (telephone != null && !telephone.isEmpty()) {
            Example paramsExample2 = new Example(Person.class);
            Example.Criteria paramsCriteria2 = paramsExample2.createCriteria();
            // 设置查询条件 多个andEqualTo串联表示 and条件查询
            paramsCriteria2.andNotEqualTo("id", id).andEqualTo("isdel", 1).andEqualTo("telephone", telephone);
            exist = personDao.selectCountByExample(paramsExample2);
            if (exist >= 1) {
                return Result.error("手机号码已被使用").toJSONString();
            }
        }

        // 身份证已存在则不添加
        String idcard = jsonObject.getString("idcard");
        if (idcard != null && !idcard.isEmpty()) {
            Example paramsExample3 = new Example(Person.class);
            Example.Criteria paramsCriteria3 = paramsExample3.createCriteria();
            // 设置查询条件 多个andEqualTo串联表示 and条件查询
            paramsCriteria3.andNotEqualTo("id", id).andEqualTo("isdel", 1).andEqualTo("idcard", idcard);
            exist = personDao.selectCountByExample(paramsExample3);
            if (exist >= 1) {
                return Result.error("身份证已被使用").toJSONString();
            }
        }

        // tagId被添加
        Integer tagId = jsonObject.getInteger("tagId");
        if (tagId != null) {
            Example paramsExample4 = new Example(Person.class);
            Example.Criteria paramsCriteria4 = paramsExample4.createCriteria();
            // 设置查询条件 多个andEqualTo串联表示 and条件查询
            paramsCriteria4.andNotEqualTo("id", id).andEqualTo("isdel", 1).andEqualTo("tagId", tagId);
            exist = personDao.selectCountByExample(paramsExample4);
            if (exist >= 1) {
                return Result.error("手环ID已被用户绑定").toJSONString();
            }
        }
        return null;
    }

    //删除人员要删除其策略和报警
    @Override
    @Transactional
    public String delete(JSONObject jsonObject) {
        List personCodes = jsonObject.getJSONArray("personCodes");
        Example example1 = new Example(Person.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andIn("personCode", personCodes).andEqualTo("isdel", 1);
        List<Person> people = personDao.selectByExample(example1);
        if (people != null && people.size() > 0) {
            Example example = new Example(Person.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("personCode", personCodes).andEqualTo("isdel", 1);
            Person person = new Person();
            person.setIsdel(0);
            int rs = personDao.updateByExampleSelective(person, example);
            if (rs > 0) {
                //删除对应的策略缓存
                List<Long> tagIds = new ArrayList<>();
                for (Person person1 : people) {
                    Long tagId = person1.getTagId();
                    if (tagId != null) {
                        tagIds.add(person1.getTagId());
                    }
                    if (tagIds != null && tagIds.size() > 0) {
                        for (Long id : tagIds) {
                            //删除人员缓存
                            this.delPersonCache(tagId);
                            //删除策略缓存和报警缓存
                            this.removePolicyAlarmCache(tagId);
                        }
                    }
                }
                return Result.ok("删除人员成功").toJSONString();
            } else {
                return Result.ok("人员已删除").toJSONString();
            }
        } else {
            return Result.ok("人员已删除").toJSONString();
        }
    }

    //查询当前部门下人员
    @Override
    public List<Person> getPersonByDepartment(String departmentCode) {
        Example example = new Example(Person.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("departmentCode", departmentCode).andEqualTo("isdel", 1);
        return personDao.selectByExample(example);
    }

    @Override
    public Person getPersonByCode(String code) {
        Person person = new Person();
        person.setIsdel(1);
        person.setPersonCode(code);
        return personDao.selectOne(person);
    }

    @Override
    public Person getPersonById(Long id) {
        Person person = new Person();
        person.setIsdel(1);
        person.setId(id);
        return personDao.selectOne(person);
    }

    //获取所有人员
    @Override
    public String getPersons() {
        List<PersonListRs> personListRs = personDao.getPersons();
        return Result.ok(personListRs).toJSONString();
    }

    //获取指定部门和其子部门下下的所有人员
    @Override
    public List<Person> getPersonsByDeparments(String departmentCode) {
        List<String> departmentCodes = departmentService.getParentSubDptCodes(departmentCode);
        Example example = new Example(Department.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("departmentCode", departmentCodes).andEqualTo("isdel", 1);
        return personDao.selectByExample(example);
    }


    //将人员信息添加到缓存
    @Override
    public void addPersonCache(Long tagId, Person person) {
        if (tagId != null) {
            String keyPre = ConstantUtil.PERSON_KEY_PRE;
            String key = redisService.genRedisKey(keyPre, tagId.toString());

            String str = JSON.toJSONString(person);
            Map map = JSONObject.parseObject(str, Map.class);
            redisService.hashAddMap(key, map, ConstantUtil.REDIS_DEFAULT_TTL);
        }
    }


    @Override
    public void delPersonCache(Long tagId) {
        if (tagId != null) {
            String key = redisService.genRedisKey(ConstantUtil.PERSON_KEY_PRE, tagId.toString());
            redisService.delete(key);
        }
    }


    //将更新后的部门及其父级部门策略添加进来
    private void updatePersonPolicy(Long tagId, String departmentCode) throws InterruptedException {
        List<String> parents = new ArrayList<>();
        Map map = new HashMap();
        //获取当前部门和父级部门编号
        List<String> allParents = departmentService.getAllParentCodes(parents, departmentCode);
        if (allParents != null && allParents.size() > 0) {
            map.put("dptCodes", allParents);
            List<Strategy> strategies = strategyDao.getStrategiesByDpt(map);
            //策略不为空,更新策略
            if (strategies != null && strategies.size() > 0) {
                this.addDptPolicy(tagId, strategies);
            }
        }
    }

    //删除人员的策略缓存和报警缓存，报警缓存有多种要全部删除
    private void removePolicyAlarmCache(Long personTagId) {
        //删除所有策略
        redisService.delete(ConstantUtil.POLICY_KEY_PRE, personTagId);
        this.removeAlarms(personTagId);
    }

    //删除人员的策略缓存和报警缓存，报警缓存有多种要全部删除
    private void removePartCache(Long personTagId, String oldDptCode) {
        String key = redisService.genRedisKey(ConstantUtil.POLICY_KEY_PRE, personTagId);
        List<RedisPolicy> redisPolicies = strategyService.getRedisPolicies(key);
        //删除策略缓存
        if (redisPolicies != null && redisPolicies.size() > 0) {
            for (RedisPolicy redisPolicy : redisPolicies) {

                String redisDptCode = redisPolicy.getStrategyUserId();
                if (redisDptCode != null && oldDptCode != null) {
                    if (redisDptCode.equals(oldDptCode)) {
                        redisService.hashDel(key, redisPolicy.getStrategyCode());
                    }
                }
            }
        }
        this.removeAlarms(personTagId);
    }

    private void removeAlarms(Long personTagId) {
        //删除报警缓存
        redisService.delete(ConstantUtil.FENCE_ALARM_KEY_PRE, personTagId);
        redisService.delete(ConstantUtil.HEART_ALARM_KEY_PRE, personTagId);
        redisService.delete(ConstantUtil.POWER_ALARM_KEY_PRE, personTagId);
        redisService.delete(ConstantUtil.SOS_ALARM_KEY_PRE, personTagId);
        redisService.delete(ConstantUtil.WRISTLET_ALARM_KEY_PRE, personTagId);
        redisService.delete(ConstantUtil.HEART_COUNT_KEY_PRE, personTagId);
    }


    //添加策略缓存,策略缓存最终要展开到带有tagId的人员上面
    private void addDptPolicy(Long tagId, List<Strategy> strategies) {
        for (Strategy strategy : strategies) {
            String timeValue = strategy.getTimeValue();
            String strategyCode = strategy.getStrategyCode();
            List<String> timeValues = Arrays.asList(timeValue.split(","));
            //构建策略缓存对象
            RedisPolicy redisPolicy = new RedisPolicy();
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

            //添加人员策略缓存
            String key = redisService.genRedisKey(ConstantUtil.POLICY_KEY_PRE, tagId);
            redisService.hashAdd(key, strategyCode, redisPolicy, ConstantUtil.REDIS_DEFAULT_TTL);
        }

    }


}
