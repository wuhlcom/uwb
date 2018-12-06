package com.zhilutec.entity;

public class Base {

    private Long id;


    private Integer page;


    private Integer listRows;


    private String sort;


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
