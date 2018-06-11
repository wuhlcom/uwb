package com.zhilutec.configs;

import org.springframework.context.annotation.Configuration;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Administrator
 * @ClassName: WebAppConfig
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2017年7月11日
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {
    //获取配置文件中图片的路径
    // @Value("${cbs.imagesPath}")
    // private String mImagesPath;

    //访问图片方法
    // @Override
    // public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //     if (mImagesPath.equals("") || mImagesPath.equals("${cbs.imagesPath}")) {
    //         String imagesPath = WebAppConfig.class.getClassLoader().getResource("").getPath();
    //         if (imagesPath.indexOf(".jar") > 0) {
    //             imagesPath = imagesPath.substring(0, imagesPath.indexOf(".jar"));
    //         } else if (imagesPath.indexOf("classes") > 0) {
    //             imagesPath = "file:" + imagesPath.substring(0, imagesPath.indexOf("classes"));
    //         }
    //         imagesPath = imagesPath.substring(0, imagesPath.lastIndexOf("/")) + "/images/";
    //         mImagesPath = imagesPath;
    //     }
    //     LoggerFactory.getLogger(WebAppConfig.class).info("imagesPath=" + mImagesPath);
    //     registry.addResourceHandler("/images/**").addResourceLocations(mImagesPath);
    //     super.addResourceHandlers(registry);
    // }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // registry.addViewController("/").setViewName("forward:/index");
        registry.addViewController("/index").setViewName("/index");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }


}