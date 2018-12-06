package com.zhilutec.common.utils;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ZlTimeUtil {

    public final static String TIMESTR = " 00:00:00";

    //获取当日零点，如2018:01:22 00:00:00
    public static Long specStamp() throws Exception {
        //减去24小时就是昨天
        // Date date=new Date(new Date().getTime()-24*60*60*1000);
        Date date = new Date(new Date().getTime());
        //只获取日期
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format1.format(date);
        //将新的日期+时间转为时间戳
        String dateTimeStr = dateStr + TIMESTR;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = simpleDateFormat.parse(dateTimeStr);
        long ts = dateTime.getTime() / 1000;
        return ts;
    }

    //返回指定起始时间和当前时间Map
    public static Map<String, Long> timeMap() throws Exception {
        Map rsMap = new HashMap();
        Long startTime = specStamp();
        rsMap.put("startTime", startTime);

        Long endTime = System.currentTimeMillis() / 1000;
        rsMap.put("endTime", endTime);
        return rsMap;
    }

    //返回星期几
    public static Integer getWeek(Long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static Time getTime(Long timestamp) {
        Time time = new Time(timestamp);
        return time;
    }

    //判断时间是否在某个范围内
    public static boolean timeBetween(String time1, String time2, String time3) throws ParseException {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");//创建日期转换对象HH:mm:ss为时分秒，年月日为yyyy-MM-dd
        try {
            Date dt1 = df.parse(time1.toString());//将字符串转换为date类型
            Date dt2 = df.parse(time2.toString());
            Date dt3 = df.parse(time3.toString());
            if (dt3.getTime() > dt1.getTime() && dt3.getTime() < dt2.getTime()) {
                //比较时间大小,如果dt1大于dt2
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    public static Time str2Time(String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = simpleDateFormat.parse(time);
        return new Time(date.getTime());
    }

    //当前月份的开始时间和结束时间
    public static Map<String,Long> seMonth() throws ParseException {
        Map<String,Long> rsMap = new HashMap();
        Calendar cale = null;
        // 获取当月第一天和最后一天
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String firstday, lastday;
        // 获取前月的第一天
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format1.format(cale.getTime());
        // 获取前月的最后一天
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        lastday = format1.format(cale.getTime());
        // System.out.println("本月第一天和最后一天分别是 ： " + firstday + " and " + lastday);

        //将新的日期+时间转为时间戳
        String firstStr = firstday + " 00:00:00";
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date firstTime = simpleDateFormat1.parse(firstStr);
        Long first = firstTime.getTime();

        String lastStr = lastday + " 23:59:59";
        Date lastTime = simpleDateFormat1.parse(lastStr);
        Long last = lastTime.getTime();
        // System.out.println("本月第一天时间和最后一天分别是时间： " + firstTime + " and " + lastTime);
        rsMap.put("startTime", first/1000);
        rsMap.put("endTime", last/1000);
        return rsMap;
    }

    // public static void main(String[] args) throws Exception {
    //     // getWeek(1521604009000L);
    //     // Time t1=getTime(1521100000000L);
    //     // Time t1 = getTime(1521100000000L);
    //     // Time t2 = getTime(1521604010000L);
    //     // System.out.println(t1);
    //     // System.out.println(t2);
    //     String tt2 = "15:46:40";
    //     String tt1 = "11:46:50";
    //     String tt3 = "12:46:50";
    //     timeBetween(tt1, tt2, tt3);
    // }

}
