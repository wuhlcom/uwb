package com.zhilutec.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ZlTimeUtil {

    public final static String TIMESTR = " 00:00:00";


    public static Long specStamp() throws Exception {
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
    // public static void main(String[] args) throws Exception {
    // System.out.println(specStamp());
    // }

}
