package com.spider.dao;

import java.util.List;

import com.spider.model.Label;

public interface LabelMapper {
	/*
    int deleteByPrimaryKey(Integer id);

    int insert(Label record);
*/
    int insertSelective(Label record);
    int insertBatch(List<Label> labels);
    List<Integer>selectIdBatch(List<String> labelnames);


    Label selectByPrimaryKey(Integer id);
/*
    int updateByPrimaryKeySelective(Label record);

    int updateByPrimaryKey(Label record);
*/
}