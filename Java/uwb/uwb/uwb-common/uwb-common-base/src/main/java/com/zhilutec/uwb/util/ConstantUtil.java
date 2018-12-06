package com.zhilutec.uwb.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConstantUtil {

    public final static String UPGRADE = "UPGRADE";
    public final static String UPGRADE_TYPE = "upgrade";
    public final static String UPGRADE_QUERY = "query";
    public final static String UPGRADE_RESP = "resp";
    //redis中升级相关的关键字
    public final static List<String> ACCESS_REDIS_KEYS = Arrays.asList(UPGRADE);

    public final static Integer UPGRADE_STAT_SUCC = 0;
    public final static Integer UPGRADE_STAT_RUNN = 1;
    public final static Integer UPGRADE_STAT_PART = 2;
    public final static Integer UPGRADE_STAT_FAIL = 3;
    public final static Integer UPGRADE_STAT_REPEAT = 4;
    public final static Integer UPGRADE_STAT_DEFAULT = 100;
    public final static Integer UPGRADE_MSG_TYPE = 6;
    //redis前缀
    public final static String HEART_COUNT_HIGHT= "hight";
    public final static String HEART_COUNT_LOW = "low";
    public final static Integer HEART_ALARM_COUNT = 3;

    //围栏报警消息
    public final static String FENCE_LEAVE = "非法离开区域:";
    public final static String FENCE_ENTER = "非法进入区域:";

    //围栏策略方向
    public final static Integer FENCE_IN = 0;
    public final static Integer FENCE_OUT = 1;

    //定位引擎消息类型
    public final static Integer ENGINE_COOR = 2;
    public final static Integer ENGINE_ARLARM = 3;
    public final static Integer ENGINE_STATUS = 7;

    //读取不到状态数据
    public final static Integer STATUS_NONE = 255;
    public final static String STATUS_ERROR = "ERROR";

    //心率阀值
    public final static Integer HEART_UP_THRESHOLD = 120;
    public static Integer HEART_LOW_THRESHOLD = 40;
    //电量阀值
    public final static Integer POWER_LOW_THRESHOLD = 20;
    public final static Integer POWER_UP_THRESHOLD = 100;
    public final static Integer ZERO_THRESHOLD = 0;


    //SOS,腕带状态
    public final static Integer STATUS_OFF = 0;
    public final static Integer STATUS_ON = 1;

    //实时坐标消息的type类型
    public final static Integer COOR_NORMAL = 2;
    public final static Integer COOR_FENCE = 3;
    public final static Integer COOR_HEART = 4;
    public final static Integer COOR_POWER = 5;
    public final static Integer COOR_SOS = 6;
    public final static Integer COOR_WRISTLET = 7;

    //报警入库时的报警类型
    public final static Integer FENCE_ALARM = 0;
    public final static Integer HEART_ALARM = 1;
    public final static Integer COOR_ALARM = 2;
    public final static Integer POWER_ALARM = 3;
    public final static Integer SOS_ALARM = 4;
    public final static Integer WRISTLET_ALARM = 5;

    //报警级别
    public final static Integer ALARM_TIP = 0;
    public final static Integer ALARM_COMM = 1;
    public final static Integer ALARM_URGEN = 2;

    public final static Integer POLYGON = 0; //多边形
    public final static Integer CIRCLE = 1;//圆
    public final static Integer OVAL = 2;//椭圆
    public final static Integer RECTANGLE = 3;//矩形

    //指定顶级部门的编号
    public final static String TOP_DEPARTMENT_CODE = "100";

    //redis缓存前缀说明
    // 1 DEPARTMENT 组织或部门信息
    // 2 FENCE 围栏信息
    // 3 LEVEL 职务级别
    // 4 PERSON 人员信息
    // 5 POLICY 策略信息
    // 6 POSITION 职务
    // 7 UPGRADE 升级过程状态信息
    // 8 STATUS  标签状态
    // 9 COORDINATE 标签最后一次坐标
    // 10 FENCE_ALARM 围栏报警
    // 11 POWER_ARLARM 电量报警
    // 12 SOS_ALARM 求救报警
    // 13 WRISTLET_ARLARM 腕带报警
    // 14 HEART_ARLARM_COUNT 心率报警计数器
    public final static String POLICY_KEY_PRE = "POLICY";
    public final static String PERSON_KEY_PRE = "PERSON";
    public final static String FENCE_KEY_PRE = "FENCE";
    public final static String DEPARTMENT_KEY_PRE = "DEPARTMENT";
    public final static String LEVEL_KEY_PRE = "LEVEL";
    public final static String POSITION_KEY_PRE = "POSITION";
    public final static String STATUS_KEY_PRE = "STATUS";
    public final static String COORDINATE_KEY_PRE = "COORDINATE";

    public final static String FENCE_ALARM_KEY_PRE = "FENCE_ALARM";
    public final static String HEART_ALARM_KEY_PRE = "HEART_ALARM";
    public final static String POWER_ALARM_KEY_PRE = "POWER_ALARM";
    public final static String SOS_ALARM_KEY_PRE = "SOS_ALARM";
    public final static String WRISTLET_ALARM_KEY_PRE = "WRISTLET_ALARM";
    public final static String HEART_COUNT_KEY_PRE = "HEART_ALARM_COUNT";

    public final static String ACCOUNT = "ACCOUNT";
    public final static String ACCOUNT_PRE = ACCOUNT + ":";
    //redis中所有resource服务中使用的关键字
    public final static List<String> REDIS_KEYS = Arrays.asList(POLICY_KEY_PRE,
            PERSON_KEY_PRE,
            FENCE_KEY_PRE,
            DEPARTMENT_KEY_PRE,
            LEVEL_KEY_PRE,
            POSITION_KEY_PRE,
            STATUS_KEY_PRE,
            COORDINATE_KEY_PRE,
            FENCE_ALARM_KEY_PRE,
            HEART_ALARM_KEY_PRE,
            POWER_ALARM_KEY_PRE,
            SOS_ALARM_KEY_PRE,
            WRISTLET_ALARM_KEY_PRE,
            HEART_COUNT_KEY_PRE,
            ACCOUNT
    );

    public final static Integer ALARM_ON = 1;
    public final static Integer ALARM_OFF = 0;


    public final static Long POLICY_TIMEOUT = 604800L;
    public final static Long REDIS_DEFAULT_TTL = -1L;

    public final static List<Integer> WARNING_TYPE = new ArrayList<>();
    public final static List<Integer> getWarningType() {
        WARNING_TYPE.add(1);
        WARNING_TYPE.add(2);
        WARNING_TYPE.add(3);
        return WARNING_TYPE;
    }

   public final static Integer FILE_ON=1;
   public final static Integer FILE_OFF=0;    

}
