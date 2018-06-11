package com.zhilutec;

import com.zhilutec.controller.ControllerTest;
import com.zhilutec.service.FastDfsTest;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
//
// @RunWith(Suite.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@org.junit.runners.Suite.SuiteClasses({FastDfsTest.class, ControllerTest.class})
public class Suite {
    // 类中不需要编写代码
}