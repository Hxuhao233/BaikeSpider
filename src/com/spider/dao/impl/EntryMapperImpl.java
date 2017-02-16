package com.spider.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spider.dao.EntryMapper;
import com.spider.model.Entry;

@Repository
@Transactional
public class EntryMapperImpl implements EntryMapper{

	@Resource(name="sqlSessionTemplate")
	private SqlSessionTemplate sqlSession;
	
	@Override
	public int insertBatch(List<Entry> entrys) {
		// TODO Auto-generated method stub
		return sqlSession.insert("com.spider.dao.EntryMapper.insertBatch",entrys);
	}

	@Override
	public int insertSelective(Entry record) {
		// TODO Auto-generated method stub
		return sqlSession.insert("com.spider.dao.EntryMapper.insertSelective",record);
	}

}
