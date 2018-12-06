package com.zhilutec.dbs.entities;

import java.util.List;

public class PathRoute {
    private String routeId; //路由ID
    private String path ;
    private String url;//业务服务域名或ip
    private List<FilterInfo> filters=null;
    private Integer available;//是否使用
    private Integer order;//路由优先级
    private Integer type;//路由类型，0 自动发现，1 来自配置文件，2 来自reids 3 来自配置类 4 其它
    private String ruleType="Path";//路由规则类型

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<FilterInfo> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterInfo> filters) {
        this.filters = filters;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }
}
