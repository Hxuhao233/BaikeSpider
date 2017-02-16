package com.spider.model;

public class EntryListURLQueue {
    private Integer id;

    private String listUrl;

    private String listUrlMd5;

    private Byte status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getListUrl() {
        return listUrl;
    }

    public void setListUrl(String listUrl) {
        this.listUrl = listUrl == null ? null : listUrl.trim();
    }

    public String getListUrlMd5() {
        return listUrlMd5;
    }

    public void setListUrlMd5(String listUrlMd5) {
        this.listUrlMd5 = listUrlMd5 == null ? null : listUrlMd5.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}