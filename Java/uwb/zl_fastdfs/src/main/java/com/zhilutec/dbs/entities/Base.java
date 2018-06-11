package com.zhilutec.dbs.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

public class Base {
    @Id
    @Column(name = "id")
    private Long id;

    @Transient
    private Integer page;

    @Transient
    private Integer listRows;

    @Transient
    private String sort;

    @Transient
    private String order;

    public String getOrder() {
        return order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getListRows() {
        return listRows;
    }

    public void setListRows(Integer listRows) {
        this.listRows = listRows;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
