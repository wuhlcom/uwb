package com.zhilutec.cloud.pojo;

// predicateParams.put("pattern", "/baidu");
// 每个Route Predicate的参数不同，详情在官网文档查看对应的Route Predicate配置示例，
// 然而官方文档也很坑，比如Path Route的- Path=/foo/{segment}，把参数给省略了。
// 还是得看源码，在包org.springframework.cloud.gateway.handler.
// predicate里有Spring Cloud Gateway所有的Predicate，打开对应的RoutePredicateFactory的实现类，
// 实现类中的内部类Config就是该Predicate支持的参数。
/**
 * predicates:
 * - Path=/uwb/gateway/**
 * - Method=GET
 * 这里定义路由表达方式yml配置文件中的格式靠齐
 */
public class RouteInfo {

    private String routeType; //路由类型
    private String route;//路由信息

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    @Override
    public String toString() {
        return "RouteInfo{" +
                "routeType='" + routeType + '\'' +
                ", route='" + route + '\'' +
                '}';
    }
}
