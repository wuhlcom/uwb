package com.zhilutec.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhilutec.dbs.entities.PathRoute;
import com.zhilutec.services.IRouteRefreshService;
import com.zhilutec.services.IRouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.util.List;


@Controller
@RequestMapping(value = "/gateway")
public class RouteMvcController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IRouteService routeService;

    @Resource
    IRouteRefreshService routeRefreshService;


    @GetMapping(value = "/login")
    public String login() {
        // ModelAndViewContainer modelAndViewContainer = new ModelAndViewContainer();
        // modelAndViewContainer.setViewName("login");
        // modelAndViewContainer.setView("login");
        // return modelAndViewContainer;
        // ModelAndView modelAndView = new ModelAndView();
        // modelAndView.setViewName("login");
        return "login";
    }

    @RequestMapping(value = "/routes", method = RequestMethod.POST)
    public String listRoutes(@RequestBody JSONObject requestJson,Model model) {
        List pathRoutes = routeService.getPathRoutes(requestJson);
        model.addAttribute("routes", pathRoutes);
        return "routes";
    }

    @GetMapping(value = "/test")
    public String test() {
        // ModelAndViewContainer modelAndViewContainer = new ModelAndViewContainer();
        // modelAndViewContainer.setViewName("login");
        // modelAndViewContainer.setView("login");
        // return modelAndViewContainer;
        // ModelAndView modelAndView = new ModelAndView();
        // modelAndView.setViewName("login");
        return "test";
    }

    @GetMapping(value = "/index", produces = "text/html;charset=UTF-8")
    public String index() {
        return "index";
    }

    @GetMapping(value = "/layout")
    public String layout() {

        // List pathRoutes = routeService.getPathRoutes(requestJson);
        // //     model.addAttribute("routes", pathRoutes);
        return "layout";
    }


}
