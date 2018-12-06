package com.zhilutec.common.utils;

import com.zhilutec.dbs.entities.Warning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConstantUtil {

    //指定顶级部门的编号
    public static String TOP_DEPARTMENT_CODE = "100";

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
    public static String POLICY_KEY_PRE = "POLICY";
    public static String PERSON_KEY_PRE = "PERSON";
    public static String FENCE_KEY_PRE = "FENCE";
    public static String DEPARTMENT_KEY_PRE = "DEPARTMENT";
    public static String LEVEL_KEY_PRE = "LEVEL";
    public static String POSITION_KEY_PRE = "POSITION";
    public static String STATUS_KEY_PRE = "STATUS";
    public static String COORDINATE_KEY_PRE = "COORDINATE";

    public static String FENCE_ALARM_KEY_PRE = "FENCE_ALARM";
    public static String HEART_ALARM_KEY_PRE = "HEART_ALARM";
    public static String POWER_ALARM_KEY_PRE = "POWER_ALARM";
    public static String SOS_ALARM_KEY_PRE = "SOS_ALARM";
    public static String WRISTLET_ALARM_KEY_PRE = "WRISTLET_ALARM";
    public static String HEART_COUNT_KEY_PRE = "HEART_ALARM_COUNT";

    public static List<String> REDIS_KEYS = Arrays.asList(POLICY_KEY_PRE,
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
            JWTUtil.ACCOUNT
    );

    public static Integer ALARM_ON = 1;
    public static Integer ALARM_OFF = 0;

    public static List<Integer> WARNING_TYPE = new ArrayList<>();

    public static Long POLICY_TIMEOUT = 604800L;

    public static Long REDIS_DEFAULT_TTL = -1L;

    public static List<Integer> getWarningType() {
        WARNING_TYPE.add(1);
        WARNING_TYPE.add(2);
        WARNING_TYPE.add(3);
        return WARNING_TYPE;
    }


}
