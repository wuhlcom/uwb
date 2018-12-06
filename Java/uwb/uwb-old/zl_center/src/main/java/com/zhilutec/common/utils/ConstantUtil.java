package com.zhilutec.common.utils;

public class ConstantUtil {

    //redis前缀
    public static String POLICY_KEY_PRE = "POLICY";
    public static String PERSON_KEY_PRE = "PERSON";
    public static String FENCE_KEY_PRE = "FENCE";
    public static String STATUS_KEY_PRE = "STATUS";
    public static String DEPARTMENT_KEY_PRE = "DEPARTMENT";
    public static String LEVEL_KEY_PRE = "LEVEL";
    public static String POSITION_KEY_PRE = "POSITION";
    public static String COORDINATE_KEY_PRE = "COORDINATE";

    public static String HEART_ALARM_KEY_PRE = "HEART_ALARM";
    public static String HEART_COUNT_KEY_PRE = "HEART_ALARM_COUNT";
    public static String HEART_COUNT_HIGHT= "hight";
    public static String HEART_COUNT_LOW = "low";
    public static Integer HEART_ALARM_COUNT = 3;

    public static String POWER_ALARM_KEY_PRE = "POWER_ALARM";
    public static String FENCE_ALARM_KEY_PRE = "FENCE_ALARM";
    public static String SOS_ALARM_KEY_PRE = "SOS_ALARM";
    public static String WRISTLET_ALARM_KEY_PRE = "WRISTLET_ALARM";

    //围栏报警消息
    public static String FENCE_LEAVE = "非法离开区域:";
    public static String FENCE_ENTER = "非法进入区域:";

    //围栏策略方向
    public static Integer FENCE_IN = 0;
    public static Integer FENCE_OUT = 1;

    //定位引擎消息类型
    public static Integer ENGINE_COOR = 2;
    public static Integer ENGINE_ARLARM = 3;
    public static Integer ENGINE_STATUS = 7;

    //读取不到状态数据
    public static Integer STATUS_NONE = 255;
    public static String STATUS_ERROR = "ERROR";

    //心率阀值
    public static Integer HEART_UP_THRESHOLD = 120;
    public static Integer HEART_LOW_THRESHOLD = 40;
    //电量阀值
    public static Integer POWER_LOW_THRESHOLD = 20;
    public static Integer POWER_UP_THRESHOLD = 100;
    public static Integer ZERO_THRESHOLD = 0;


    //SOS,腕带状态
    public static Integer STATUS_OFF = 0;
    public static Integer STATUS_ON = 1;

    //实时坐标消息的type类型
    public static Integer COOR_NORMAL = 2;
    public static Integer COOR_FENCE = 3;
    public static Integer COOR_HEART = 4;
    public static Integer COOR_POWER = 5;
    public static Integer COOR_SOS = 6;
    public static Integer COOR_WRISTLET = 7;

    //报警入库时的报警类型
    public static Integer FENCE_ALARM = 0;
    public static Integer HEART_ALARM = 1;
    public static Integer COOR_ALARM = 2;
    public static Integer POWER_ALARM = 3;
    public static Integer SOS_ALARM = 4;
    public static Integer WRISTLET_ALARM = 5;

    //报警级别
    public static Integer ALARM_TIP = 0;
    public static Integer ALARM_COMM = 1;
    public static Integer ALARM_URGEN = 2;

    //报警开关
    public static Integer ALARM_ON = 1;
    public static Integer ALARM_OFF = 0;

    public static Long POLICY_TIMEOUT = 604800L;
    public static Long REDIS_DEFAULT_TTL = -1L;

    public static final Integer POLYGON = 0; //多边形
    public static final Integer CIRCLE = 1;//圆
    public static final Integer OVAL = 2;//椭圆
    public static final Integer RECTANGLE = 3;//矩形

}
