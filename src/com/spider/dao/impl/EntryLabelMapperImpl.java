package com.spider.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spider.dao.EntryLabelMapper;
import com.spider.model.EntryLabel;

@Transactional
@Repository
public class EntryLabelMapperImpl implements EntryLabelMapper{

	@Resource(name="sqlSessionTemplate")
	private SqlSessionTemplate sqlSession;
	
	
	@Override
	public int insertSelective(EntryLabel record) {
		// TODO Auto-generated method stub
		return sqlSession.insert("com.spider.dao.EntryLabelMapper.insertSelective",record);
	}

	@Override
	public int insertBatch(List<EntryLabel> record) {
		// TODO Auto-generated method stub
		return sqlSession.insert("com.spider.dao.EntryLabelMapper.insertBatch",record);
	}
}
