package com.spider.dao;

import com.spider.model.URLQueue;

public interface URLQueueMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(URLQueue record);

    int insertSelective(URLQueue record);

    URLQueue selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(URLQueue record);

    int updateByPrimaryKey(URLQueue record);
}