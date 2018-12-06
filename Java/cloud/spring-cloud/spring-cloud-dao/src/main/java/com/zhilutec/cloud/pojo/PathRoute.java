package com.zhilutec.cloud.pojo;

import java.util.List;

public class PathRoute {
    private String routeId; //路由ID
    private String route ; //路径
    private String url;//业务服务域名或ip或服务名
    private List<FilterInfo> filters=null;
    private Integer available;//是否使用
    private Integer order;//路由优先级
    private Integer type;//路由类型，0 自动发现，1 来自配置文件，2 来自reids 3 来自配置类 4 其它
    private String routeType="Path";//路由规则类型
    private String urlType;//服务url类型类型

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
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

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getUrlType() {
        return urlType;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    @Override
    public String toString() {
        return "PathRoute{" +
                "routeId='" + routeId + '\'' +
                ", route='" + route + '\'' +
                ", url='" + url + '\'' +
                ", filters=" + filters +
                ", available=" + available +
                ", order=" + order +
                ", type=" + type +
                ", routeType='" + routeType + '\'' +
                ", urlType='" + urlType + '\'' +
                '}';
    }
}
