package com.spider.dao;

import com.spider.model.Entry;

public interface EntryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Entry record);

    int insertSelective(Entry record);

    Entry selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Entry record);

    int updateByPrimaryKey(Entry record);
}