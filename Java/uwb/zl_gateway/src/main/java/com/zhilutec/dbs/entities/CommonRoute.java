package com.zhilutec.dbs.entities;

import java.util.List;

public class CommonRoute {
    private String routeId; //路由ID
    private List<RouteInfo> predicates;
    private String url;//业务服务域名或ip
    private String urlType;//url类型 http或lb
    private List<FilterInfo> filters;
    private Integer available;//是否使用
    private Integer order;//路由优先级
    private Integer type=2;//路由类型，0 自动发现，1 来自配置文件，2 来自reids 3 来自配置类 4 其它


    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public List<RouteInfo> getPredicates() {
        return predicates;
    }

    public void setPredicates(List<RouteInfo> predicates) {
        this.predicates = predicates;
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

    public String getUrlType() {
        return urlType;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    @Override
    public String toString() {
        return "CommonRoute{" +
                "routeId='" + routeId + '\'' +
                ", predicates=" + predicates +
                ", url='" + url + '\'' +
                ", urlType='" + urlType + '\'' +
                ", filters=" + filters +
                ", available=" + available +
                ", order=" + order +
                ", type=" + type +
                '}';
    }
}
