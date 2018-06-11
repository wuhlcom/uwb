package com.zhilutec.common.utils;

import com.zhilutec.dbs.entities.Warning;

import java.util.ArrayList;
import java.util.List;

public class ConstantUtil {

    //指定顶级部门的编号
    public static String TOP_DEPARTMENT_CODE = "100";

    public static String POLICY_KEY_PRE = "POLICY";
    public static String PERSON_KEY_PRE = "PERSON";
    public static String FENCE_KEY_PRE = "FENCE";
    public static String DEPARTMENT_KEY_PRE = "DEPARTMENT";
    public static String LEVEL_KEY_PRE = "LEVEL";
    public static String POSITION_KEY_PRE = "POSITION";
    public static String FENCE_ALARM_KEY_PRE = "FENCE_ALARM";
    public static String HEART_ALARM_KEY_PRE = "HEART_ALARM";
    public static String POWER_ALARM_KEY_PRE = "POWER_ALARM";
    public static String SOS_ALARM_KEY_PRE = "SOS_ALARM";
    public static String WRISTLET_ALARM_KEY_PRE = "WRISTLET_ALARM";
    public static String HEART_COUNT_KEY_PRE = "HEART_ALARM_COUNT";


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
