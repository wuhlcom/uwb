package com.zhilutec.common.utils;

import java.util.Arrays;
import java.util.List;

public class ConstantUtil {

    public static final String UPGRADE = "UPGRADE";
    public static final String UPGRADE_TYPE = "upgrade";
    public static final String UPGRADE_QUERY = "query";
    public static final String UPGRADE_RESP = "resp";
    public static final List<String> REDIS_KEYS = Arrays.asList(UPGRADE);

    public static final Integer UPGRADE_STAT_SUCC = 0;
    public static final Integer UPGRADE_STAT_RUNN = 1;
    public static final Integer UPGRADE_STAT_PART = 2;
    public static final Integer UPGRADE_STAT_FAIL = 3;
    public static final Integer UPGRADE_STAT_REPEAT = 4;
    public static final Integer UPGRADE_STAT_DEFAULT = 100;
    public static final Integer UPGRADE_MSG_TYPE = 6;

    public static Long REDIS_DEFAULT_TTL = -1L;

    //报警开关
    public static Integer ALARM_ON = 1;
    public static Integer ALARM_OFF = 0;

}
