package com.zhilutec.uwb.config.redis;

//redis超时注解
public abstract class CacheContext {
    private final static ThreadLocal<Long> overTime = new ThreadLocal<Long>();

    public static void setOverTime(Long time) {
        overTime.set(time);
    }

    public static Long getOverTime() {
        return overTime.get();
    }

    public static void removeOverTime() {
        overTime.remove();
    }
}