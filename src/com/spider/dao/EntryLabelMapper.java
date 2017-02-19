package com.spider.dao;

import java.util.List;
import java.util.Map;

import com.spider.model.EntryLabel;

public interface EntryLabelMapper {
	/*
    int deleteByPrimaryKey(Integer id);

    int insert(EntryLabel record);
*/
    int insertSelective(EntryLabel record);
/*
    

    int updateByPrimaryKeySelective(EntryLabel record);

    int updateByPrimaryKey(EntryLabel record);
*/

    
	int insertBatch(List<EntryLabel> record);

	List<Integer> selectLabelIdByEntryId(Integer eid);
}