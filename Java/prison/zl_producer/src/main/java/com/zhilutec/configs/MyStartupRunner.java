package com.zhilutec.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// 我们在开发中可能会有这样的情景。需要在容器启动的时候执行一些内容。比如读取配置文件，数据库连接之类的。
// SpringBoot给我们提供了两个接口来帮助我们实现这种需求。这两个接口分别为CommandLineRunner和ApplicationRunner。他们的执行时机为容器启动完成的时候。
//当有多个MyStartupRunner1时可以使用Order来控制执行顺序
@Component
@Order(value = 2)
public class MyStartupRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>服务启动，执行自定义Runner <<<<<<<<<<<<<");
    }

}