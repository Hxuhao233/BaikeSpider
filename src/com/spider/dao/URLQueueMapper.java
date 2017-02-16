package com.spider.dao;

import java.util.List;

import com.spider.model.URLQueue;

public interface URLQueueMapper {
    int deleteByPrimaryKey(Integer id);

    //int insert(URLQueue record);

    int insertSelective(URLQueue record);
    
    int insertURLBatch(List<URLQueue>urls);
    
    URLQueue selectOne();
    
    List<URLQueue> selectBatch();
    
    //void updateBatch(List<URLQueue> queue);
    //URLQueue selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(URLQueue record);

    int updateStatus(Integer id);
    //int updateByPrimaryKey(URLQueue record);
}