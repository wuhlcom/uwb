package com.zhilutec.db;

public class CustomerRoute {
    private Integer id;
    private String routeId;
    //支持的路由类型有After、Before、Between、Cookie、Header、Host、Method、Path、Query、RemoteAddr,注意要区分大小写
    // 当前只实现了Path类型路由从数据库获取
    private String routeType;
    private String path;
    private String url;
    private Integer port;
    private String host;
    private String filterType;
    private String filter;
    private String operter;
    private Integer order;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
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

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getOperter() {
        return operter;
    }

    public void setOperter(String operter) {
        this.operter = operter;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "CustomerRoute{" +
                "id=" + id +
                ", routeId='" + routeId + '\'' +
                ", routeType='" + routeType + '\'' +
                ", path='" + path + '\'' +
                ", url='" + url + '\'' +
                ", port=" + port +
                ", host='" + host + '\'' +
                ", filterType='" + filterType + '\'' +
                ", filter='" + filter + '\'' +
                ", operter='" + operter + '\'' +
                ", order=" + order +
                '}';
    }
}
