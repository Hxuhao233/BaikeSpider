package com.spider.dao;

import java.util.List;
import java.util.Map;

import com.spider.model.Entry;

public interface EntryMapper {
	/*
    int deleteByPrimaryKey(Integer id);

    int insert(Entry record);



    Entry selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Entry record);

    int updateByPrimaryKey(Entry record);
    */

    int insertSelective(Entry record);
	
	int insertBatch(List<Entry> entrys);


	List<Entry> selectByName(Map<String, Object> param);
}