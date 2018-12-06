package com.zhilutec.configs.redis;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
public class CacheOverTimeAspect {
    @Before("@annotation(com.zhilutec.configs.redis.Overtime)")
    public void before(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Overtime overTime = method.getAnnotation(Overtime.class);
        if (overTime != null && overTime.value() > 0) {
            CacheContext.setOverTime(overTime.value());//在上下文中设置超时时间
        }
    }

    @After("@annotation(com.zhilutec.configs.redis.Overtime)")
    public void after(JoinPoint joinPoint) {
        CacheContext.removeOverTime();//清除上下文超时时间
    }
}