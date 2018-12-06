package com.zhilutec.dbs.entities;

/**
 * filters:
 * - AddRequestHeader=X-Request-Foo, Bar
 * - AddRequestParameter=foo, bar
 * - name: Hystrix
 * args:
 * name: fallbackcmd
 * fallbackUri: forward:/incaseoffailureusethis
 * - RewritePath=/consumingserviceendpoint, /backingserviceendpoint
 * 这里定义过滤条件尽量向yml配置文件中的格式靠齐
 */
public class FilterInfo {
    private String filterType;
    private String filter;

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

    @Override
    public String toString() {
        return "FilterInfo{" +
                "filterType='" + filterType + '\'' +
                ", filter='" + filter + '\'' +
                '}';
    }
}
