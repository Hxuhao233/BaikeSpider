package com.spider.dao;

import java.util.List;

import com.spider.model.EntryLabel;

public interface EntryLabelMapper {
	/*
    int deleteByPrimaryKey(Integer id);

    int insert(EntryLabel record);
*/
    int insertSelective(EntryLabel record);
/*
    EntryLabel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EntryLabel record);

    int updateByPrimaryKey(EntryLabel record);
*/

	int insertBatch(List<EntryLabel> record);
}