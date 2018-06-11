package com.zhilutec.configs.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class StrategyTask {
    //
    // //6-22点每个时间15分钟时执行一次
    // @Scheduled(cron = "0 15 6,8,10,12,14,16,18,20,22 * * ?")
    // public void flushStrategy(){
    //     System.out.println("flush strategy");
    // }
    //
    // //每五秒执行
    // @Scheduled(cron = "0 10 * * * ?")
    // public void printSay() {
    //     System.out.println("This is a say method!"+new Date());
    // }
}
