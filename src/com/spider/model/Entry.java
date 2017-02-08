package com.spider.model;

public class Entry {
    private Integer id;

    private String entryname;

    private String publisher;

    private String content;

    private String pictureaddr;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEntryname() {
        return entryname;
    }

    public void setEntryname(String entryname) {
        this.entryname = entryname == null ? null : entryname.trim();
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher == null ? null : publisher.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getPictureaddr() {
        return pictureaddr;
    }

    public void setPictureaddr(String pictureaddr) {
        this.pictureaddr = pictureaddr == null ? null : pictureaddr.trim();
    }
}