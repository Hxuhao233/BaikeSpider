package com.spider.model;

public class Label {
    private Integer id;

    private String labelname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabelname() {
        return labelname;
    }

    public void setLabelname(String labelname) {
        this.labelname = labelname == null ? null : labelname.trim();
    }
}