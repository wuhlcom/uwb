package com.zhilutec.configs.startup;

import com.zhilutec.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Order(value = 1)
public class RedisInit implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IPersonService personService;

    @Resource
    IStrategyService strategyService;

    @Resource
    IFenceService fenceService;

    @Resource
    IDepartmentService departmentService;

    @Resource
    ILevelService levelService;

    @Resource
    IPositionService positionService;

    @Override
    public void run(String... args) throws Exception {
        logger.info(">>>>>>>>>>>>>>>服务启动执行:清空缓存数据<<<<<<<<<<<<");
        personService.flushdDbData();

        logger.info(">>>>>>>>>>>>>>>服务启动执行:初始化人员缓存数据<<<<<<<<<<<<");
        personService.personCacheInit();

        logger.info(">>>>>>>>>>>>>>>服务启动执行:初始化策略缓存数据<<<<<<<<<<<<");
        strategyService.policyCacheInit();

        logger.info(">>>>>>>>>>>>>>>服务启动执行:初始化围栏缓存数据<<<<<<<<<<<<");
        fenceService.fenceCacheInit();

        logger.info(">>>>>>>>>>>>>>>服务启动执行:初始化部门缓存数据<<<<<<<<<<<<");
        departmentService.departmentCacheInit();

        logger.info(">>>>>>>>>>>>>>>服务启动执行:初始化职务缓存数据<<<<<<<<<<<<");
        positionService.positionCacheInit();

        logger.info(">>>>>>>>>>>>>>>服务启动执行:初始化级别缓存数据<<<<<<<<<<<<");
        levelService.levelCacheInit();
    }

}