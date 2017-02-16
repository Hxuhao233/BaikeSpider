package com.spider.dao;

import java.util.List;

import com.spider.model.EntryListURLQueue;
import com.spider.model.URLQueue;

public interface EntryListURLQueueMapper {
    //int deleteByPrimaryKey(Integer id);

    //int insert(EntryListURLQueue record);

    int insertSelective(EntryListURLQueue record);

    int insertURLBatch(List<EntryListURLQueue>urls);
      
    EntryListURLQueue selectOne();
    
    List<EntryListURLQueue> selectBatch();
    
    int updateStatus(Integer id);
    //EntryListURLQueue selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EntryListURLQueue record);

    //int updateByPrimaryKey(EntryListURLQueue record);
}