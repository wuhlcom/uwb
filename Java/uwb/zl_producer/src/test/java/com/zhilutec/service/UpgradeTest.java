package com.zhilutec.service;

import com.zhilutec.services.IUpgradeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UpgradeTest {

    @Resource
    IUpgradeService upgradeService;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void getVersion() throws Exception {
        upgradeService.getVersion();
    }

}
