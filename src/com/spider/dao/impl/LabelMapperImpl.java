package com.spider.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spider.dao.LabelMapper;
import com.spider.model.Label;

@Repository
@Transactional
public class LabelMapperImpl implements LabelMapper{
	
	@Resource(name="sqlSessionTemplate")
	private SqlSessionTemplate sqlSession;

	public int insertSelective(Label record) {
		// TODO Auto-generated method stub
		return sqlSession.insert("com.spider.dao.LabelMapper.insertSelective",record);
	}


	@Override
	public int insertBatch(List<Label> labels) {
		// TODO Auto-generated method stub
		return sqlSession.insert("com.spider.dao.LabelMapper.insertBatch",labels);
	}

	@Override
	public List<Integer> selectIdBatch(List<String> labelnames) {
		// TODO Auto-generated method stub
		return sqlSession.selectList("com.spider.dao.LabelMapper.selectIdBatch",labelnames);
	}
	
	
}
