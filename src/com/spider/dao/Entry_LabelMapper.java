package com.spider.dao;

import com.spider.model.Entry_Label;

public interface Entry_LabelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Entry_Label record);

    int insertSelective(Entry_Label record);

    Entry_Label selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Entry_Label record);

    int updateByPrimaryKey(Entry_Label record);
}