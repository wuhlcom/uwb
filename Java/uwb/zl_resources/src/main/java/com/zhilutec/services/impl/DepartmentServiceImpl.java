package com.zhilutec.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.result.Result;
import com.zhilutec.common.result.ResultCode;
import com.zhilutec.common.utils.ConstantUtil;
import com.zhilutec.common.utils.TreeUtils;
import com.zhilutec.common.validators.DepartmentValidator;
import com.zhilutec.dbs.daos.DepartmentDao;
import com.zhilutec.dbs.daos.PersonDao;
import com.zhilutec.dbs.entities.Department;
import com.zhilutec.dbs.entities.Person;
import com.zhilutec.dbs.entities.Strategy;
import com.zhilutec.dbs.pojos.PersonDepartmentParam;
import com.zhilutec.dbs.pojos.RedisPolicy;
import com.zhilutec.services.IDepartmentService;
import com.zhilutec.services.IRedisService;
import com.zhilutec.services.IStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

@Service
public class DepartmentServiceImpl implements IDepartmentService {

    private static final String DPT_CODE = "departmentCode";
    private static final String DPT_PARENT = "parentCode";

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    PersonDao personDao;

    @Resource
    IStrategyService strategyService;

    @Resource
    IRedisService redisService;

    @Override
    @Transactional
    public void departmentCacheInit() {
        List<Department> departments = departmentDao.getDepartments();
        for (Department department : departments) {
            this.addDptCache(department);
        }
    }

    //添加部门
    //更新部门中添加的人员
    @Transactional
    @Override
    public String add(JSONObject jsonObject) throws InterruptedException {
        Result rs = DepartmentValidator.addVal(jsonObject);
        if ((Integer) rs.get("errcode") != ResultCode.SUCCESS.getCode())
            return rs.toJSONString();

        String parentCode = jsonObject.getString(DPT_PARENT);
        List<String> parentCodes = new ArrayList<>();
        this.getAllParentCodes(parentCodes, parentCode);
        if (parentCodes.size() >= 5) {
            return Result.error("最多只能添加五级部门").toJSONString();
        }

        Department newDepartment = new Department();


        String uuid = UUID.randomUUID().toString();
        jsonObject.put(DPT_CODE, uuid);
        newDepartment.setDepartmentCode(uuid);
        String departmentName = jsonObject.getString("departmentName");
        newDepartment.setDepartmentName(departmentName);


        newDepartment.setParentCode(parentCode);

        String remark = jsonObject.getString("remark");
        newDepartment.setRemark(remark);
        Long createdAt = System.currentTimeMillis() / 1000;
        newDepartment.setCreatedAt(createdAt);

        Integer insert = departmentDao.insertSelective(newDepartment);
        if (insert > 0) {
            //添加部门缓存
            this.addDptCache(newDepartment);
            //刷新人员部门,当选择了部门人员时
            List persons = jsonObject.getJSONArray("persons");
            if (persons != null && !persons.isEmpty()) {
                //更新人员部门
                this.updatePersonDeparment(persons, uuid);
                //刷新人员策略
                this.updatePersonPolicy(jsonObject, uuid);
            }
            return Result.ok("新增部门成功").toJSONString();
        } else {
            return Result.error("新增部门失败").toJSONString();
        }
    }

    // 修改部门人员要刷新策略缓存
    @Transactional
    @Override
    public String update(JSONObject jsonObject) throws InterruptedException {
        Result rs = DepartmentValidator.updateVal(jsonObject);
        if ((Integer) rs.get("errcode") != ResultCode.SUCCESS.getCode())
            return rs.toJSONString();
        String departmentCode = jsonObject.getString(DPT_CODE);
        String parentCode = jsonObject.getString(DPT_PARENT);
        if (departmentCode.equals(parentCode)) {
            return Result.error("上级部门不能选择为当前部门").toJSONString();
        }

        List<String> parentCodes = new ArrayList<>();
        this.getAllParentCodes(parentCodes, parentCode);
        if (parentCodes.size() >= 5) {
            return Result.error("最多只能添加五级部门").toJSONString();
        }

        String departmentName = jsonObject.getString("departmentName");
        String remark = jsonObject.getString("remark");

        Department newDepartment = new Department();
        if (departmentName != null && !departmentName.isEmpty()) {
            newDepartment.setDepartmentName(departmentName);
        }

        if (parentCode != null && !parentCode.isEmpty()) {
            newDepartment.setParentCode(parentCode);
        }

        if (remark != null && !remark.isEmpty()) {
            newDepartment.setRemark(remark);
        }

        Example example = new Example(Department.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(DPT_CODE, departmentCode).andEqualTo("isdel", 1);

        Integer updateRs = departmentDao.updateByExampleSelective(newDepartment, example);

        if (updateRs > 0) {
            Department department = this.getDepartmentByCode(departmentCode);
            //更新部门缓存
            this.addDptCache(department);
        }

        List persons = jsonObject.getJSONArray("persons");

        Integer updatePerson = 0;
        //更新部门时添加了人员,部门如果存在策略要展开策略到人员下面
        if (persons != null && persons.size() > 0) {
            //刷新人员部门
            updatePerson = this.updatePersonDeparment(persons, departmentCode);
            //刷新人员策略
            this.updatePersonPolicy(jsonObject, departmentCode);
        }

        if (updatePerson > 0 || updateRs > 0) {
            return Result.ok("修改部门成功").toJSONString();
        } else {
            return Result.error("修改部门失败").toJSONString();
        }
    }

    /**
     * 更新人员的部门
     */
    private int updatePersonDeparment(List persons, String departmentCode) {
        PersonDepartmentParam personDepartmentParam = new PersonDepartmentParam();
        personDepartmentParam.setDepartmentCode(departmentCode);
        personDepartmentParam.setPersons(persons);
        return personDao.batchUpdateDepartment(personDepartmentParam);
    }

    /**
     * 查询部门的策略列表
     * 查询要添加的人员列表
     * 将部门策略列表中的策略添加到人员策略缓存中
     */
    private void updatePersonPolicy(JSONObject jsonObject, String departmentCode) throws InterruptedException {
        List persons = jsonObject.getJSONArray("persons");
        //判断添加部门时是否添加人员
        if (persons != null && persons.size() > 0) {
            // 查询部门的策略列表
            Example example = new Example(Person.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("personCode", persons).andEqualTo("isdel", 1).andIsNotNull("tagId");
            // 查询要添加的人员列表
            List<Person> people = personDao.selectByExample(example);
            List<Strategy> strategies = strategyService.getStrategyByUserId(departmentCode);
            if (strategies != null && strategies.size() > 0) {
                for (Strategy strategy : strategies) {
                    if (people != null && people.size() > 0) {
                        String timeValue = strategy.getTimeValue();
                        String strategyCode = strategy.getStrategyCode();
                        List<String> timeValues = Arrays.asList(timeValue.split(","));
                        //构建策略缓存对象
                        RedisPolicy redisPolicy = new RedisPolicy();
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

                        for (Person person : people) {
                            //人员如果没有配置tagId则不添加策略
                            Long tagId = person.getTagId();
                            if (tagId != null) {
                                //添加人员策略缓存
                                String key = redisService.genRedisKey(ConstantUtil.POLICY_KEY_PRE, tagId);
                                redisService.hashAdd(key, strategyCode, redisPolicy, ConstantUtil.REDIS_DEFAULT_TTL);
                            }
                        }
                    }
                }

            }
        }
    }


    /**
     * 根据部门编号查询部门，将isdel设置为空
     */
    @Override
    public Department getDepartmentByCode(String code) {
        Department department = null;
        if (code != null) {
            try {
                department = new Department();
                department.setDepartmentCode(code);
                department.setIsdel(1);
                department = departmentDao.selectOne(department);
                if (department != null) {
                    department.setIsdel(null);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return department;
    }


    /**
     * 递归查询上级部门名
     */
    @Override
    public List<String> getAllParents(List<String> parents, String parentCode) {
        Department department = this.getDepartmentByCode(parentCode);
        if (department != null) {
            parentCode = department.getParentCode();
            parents.add(department.getDepartmentName());
            if (!department.getDepartmentCode().equals(ConstantUtil.TOP_DEPARTMENT_CODE)) {
                getAllParents(parents, parentCode);
            }
        }
        return parents;
    }

    /**
     * 递归查询上级部门编号
     */
    @Override
    public List<String> getAllParentCodes(List<String> parents, String parentCode) {
        Department department = this.getDepartmentByCode(parentCode);
        if (department != null) {
            parentCode = department.getParentCode();

            String departmentCode = department.getDepartmentCode();
            if (departmentCode != null) {
                parents.add(departmentCode);
            }

            if (!departmentCode.equals(ConstantUtil.TOP_DEPARTMENT_CODE)) {
                getAllParentCodes(parents, parentCode);
            }
        }
        return parents;
    }


    /**
     * 查询所有上级部门对象
     */
    private List<Department> getParentList(List<Department> departments, String parentCode) {
        Department parentDpt = this.getDepartmentByCode(parentCode);
        if (parentDpt != null) {
            departments.add(parentDpt);
            parentCode = parentDpt.getParentCode();
            String departmentCode = parentDpt.getDepartmentCode();
            if (!departmentCode.equals(ConstantUtil.TOP_DEPARTMENT_CODE)) {
                getParentList(departments, parentCode);
            }
        }
        return departments;
    }

    /**
     * 上级部门树
     */
    @Override
    public Department getParentTree(List<Department> departments, String parentCode) {
        List<Department> parentDpts = getParentList(departments, parentCode);
        Department root = parentDpts.get(parentDpts.size() - 1);
        //递归查询
        TreeUtils.createTree(departments, root, DPT_CODE, DPT_PARENT, "children");
        return root;
    }


    /**
     * 返回部门树，根据parentCode查询
     */
    @Override
    public Department getDepartmentsTree() {
        List<Department> departments = departmentDao.getDepartments();
        for (Department department : departments) {
            if (department.getRemark() == null || department.getRemark().isEmpty()) {
                department.setRemark("");
            }
        }
        Department root = this.getDepartmentByCode(ConstantUtil.TOP_DEPARTMENT_CODE);
        if (root.getRemark() == null || root.getRemark().isEmpty()) {
            root.setRemark("");
        }
        //递归查询
        TreeUtils.createTree(departments, root, DPT_CODE, DPT_PARENT, "children");
        return root;
    }

    @Override
    public String getDepartmentsTreeRs() {
        Department department = this.getDepartmentsTree();
        return Result.ok(department).toJSONString();
    }

    //删除部门
    @Transactional
    @Override
    public String delete(JSONObject jsonObject) {
        Result rs = DepartmentValidator.delVal(jsonObject);
        if ((Integer) rs.get("errcode") != ResultCode.SUCCESS.getCode())
            return rs.toJSONString();

        String departmentCode = jsonObject.getString(DPT_CODE);
        if (departmentCode.equals(ConstantUtil.TOP_DEPARTMENT_CODE)) {
            return Result.error("要删除顶级部门请联系管理员").toJSONString();
        }
        //删除当前部门
        Example example = new Example(Department.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(DPT_CODE, departmentCode).andEqualTo("isdel", 1);

        Department dpt = JSONObject.parseObject(jsonObject.toJSONString(), Department.class);
        dpt.setIsdel(0);
        Integer del = departmentDao.updateByExampleSelective(dpt, example);
        //更新当前部门人员到顶级部门
        this.deletePersonDpt(departmentCode);
        if (del > 0) {
            List<Department> departments = new ArrayList<>();
            departments = this.getSubDpt(departments, departmentCode);
            List<String> departmentCodes = new ArrayList<>();
            //把当前部门也添加进来
            departmentCodes.add(departmentCode);
            if (departments != null && departments.size() > 0) {
                //删除当前部门下的所有子部门
                this.deleteSubDpt(departments);
                for (Department department : departments) {
                    departmentCodes.add(department.getDepartmentCode());
                }
            }

            //删除部门相关的策略和报警缓存
            strategyService.deleteByDptUser(departmentCodes);
            //删除部门缓存
            for (String code : departmentCodes) {
                this.deleteDptCache(code);
            }
            return Result.ok("删除部门成功").toJSONString();
        } else {
            return Result.error("部门已删除").toJSONString();
        }
    }

    //查询所有子部门
    private List<Department> getSubDpt(List<Department> departments, String parentCode) {
        Example example = new Example(Department.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(DPT_PARENT, parentCode).andEqualTo("isdel", 1);
        List<Department> departments1 = departmentDao.selectByExample(example);
        if (departments1 != null && departments1.size() > 0) {
            departments.addAll(departments1);
            for (Department department : departments1) {
                parentCode = department.getDepartmentCode();
                getSubDpt(departments, parentCode);
            }
        }
        return departments;
    }

    // 更新人员到顶级部门
    private void deletePersonDpt(String departmentCode) {
        //更新部门下的人员到顶级部门
        Example example = new Example(Person.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(DPT_CODE, departmentCode).andEqualTo("isdel", 1);

        Person person = new Person();
        person.setDepartmentCode(ConstantUtil.TOP_DEPARTMENT_CODE);
        personDao.updateByExampleSelective(person, example);
    }

    //删除所有子部门及更新子部门人员到顶级部门
    private void deleteSubDpt(List<Department> departments) {
        if (departments != null && departments.size() > 0) {
            List<String> departmentCodes = new ArrayList<>();
            for (Department department : departments) {
                departmentCodes.add(department.getDepartmentCode());
                this.deletePersonDpt(department.getDepartmentCode());
            }
            //删除所有子部门
            Example example = new Example(Department.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn(DPT_CODE, departmentCodes).andEqualTo("isdel", 1);

            Department dpt = new Department();
            dpt.setIsdel(0);
            departmentDao.updateByExampleSelective(dpt, example);
        }
    }

    @Override
    public Department getDepartmentById(Long id) {
        Department department = new Department();
        department.setIsdel(1);
        department.setId(id);
        return departmentDao.selectOne(department);
    }

    //查询所有部门
    @Override
    public String getDepartments() {
        List<Department> departments = departmentDao.getDepartments();
        return Result.ok(departments).toJSONString();
    }

    //查询当前部门和所有子部门
    @Override
    public List<Department> getParentSubDpt(String deparmentCode) {
        Department department = this.getDepartmentByCode(deparmentCode);
        List<Department> departments = new ArrayList<>();
        if (department != null) {
            departments = this.getSubDpt(departments, deparmentCode);
            departments.add(department);
        }
        return departments;
    }

    //查询当前部门和所有子部门编号
    @Override
    public List<String> getParentSubDptCodes(String deparmentCode) {
        List<Department> departments = getParentSubDpt(deparmentCode);
        List<String> codes = new ArrayList<>();
        for (Department department : departments) {
            codes.add(department.getDepartmentCode());
        }
        return codes;
    }


    @Override
    public void deleteDptCache(String departmentCode) {
        String key = redisService.genRedisKey(ConstantUtil.DEPARTMENT_KEY_PRE, departmentCode);
        redisService.delete(key);
    }

    @Override
    public void addDptCache(Department department) {
        String departmentCode = department.getDepartmentCode();
        String keyPre = ConstantUtil.DEPARTMENT_KEY_PRE;
        String key = redisService.genRedisKey(keyPre, departmentCode);

        String str = JSON.toJSONString(department);
        Map map = JSONObject.parseObject(str, Map.class);
        redisService.hashAddMap(key, map, ConstantUtil.REDIS_DEFAULT_TTL);
    }
}
