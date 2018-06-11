package com.zhilutec.tools;


import com.alibaba.fastjson.JSONObject;
import com.zhilutec.common.utils.RestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestUtilTest {

    @Test
    public void getFileInfo(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fileName","show");
        Map map = RestUtil.getFileInfo(jsonObject);
        System.out.println(map);
    }
}
