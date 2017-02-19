package com.spider.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spider.dao.URLQueueMapper;
import com.spider.model.URLQueue;

@Repository
@Transactional
public class URLQueueMapperImpl implements URLQueueMapper{
	
	

	@Resource(name="sqlSessionTemplate")
	private SqlSessionTemplate sqlSession;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(URLQueue record) {
		// TODO Auto-generated method stub
		int ret =  sqlSession.insert("com.spider.dao.URLQueueMapper.insertSelective", record);
		System.out.println("insert " + ret);
		return ret;
	}

	@Override
	public URLQueue selectOne() {
		// TODO Auto-generated method stub
		URLQueue url = sqlSession.selectOne("com.spider.dao.URLQueueMapper.selectOne");
		sqlSession.update("com.spider.dao.URLQueueMapper.updateStatus",url.getId());
		return url;
	}

	@Override
	public int updateByPrimaryKeySelective(URLQueue record) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int updateStatus(Integer id) {
		// TODO Auto-generated method stub
		return sqlSession.update("com.spider.dao.URLQueueMapper.updateStatus",id);
	}

	@Override
	public int insertURLBatch(List<URLQueue> urls) {
		// TODO Auto-generated method stub
		/*
		for(URLQueue url : urls){
			System.out.println(url.getUrl());
		}*/
		return sqlSession.insert("com.spider.dao.URLQueueMapper.insertURLBatch",urls);
	}
	

	// 批量查询(并且更新)
	@Override
	public List<URLQueue> selectBatch() {
		// TODO Auto-generated method stub
		List<URLQueue> queue = sqlSession.selectList("com.spider.dao.URLQueueMapper.selectBatch");
		if(queue.size()>0)
			sqlSession.update("com.spider.dao.URLQueueMapper.updateBatch", queue);
		return queue;
	}
	
	public static void main(String[] args){
		
		ApplicationContext ac1 = new ClassPathXmlApplicationContext(new String[] {"spring-mybatis.xml"});
		
		URLQueueMapper mapper = (URLQueueMapper) ac1.getBean("URLQueueMapperImpl");
		URLQueue url = new URLQueue();
		URLQueue url2 = new URLQueue();
		URLQueue url3 = new URLQueue();
		url.setUrl("asassasasasq");
		url2.setUrl("233233233");
		url3.setUrl("2332332334");
		List<URLQueue> urls = new ArrayList<>();
		urls.add(url);
		urls.add(url2);
		urls.add(url3);

		mapper.insertURLBatch(urls);
		
		/*
		List<URLQueue> temp = mapper.selectMany();
		for(int i=0;i<temp.size();i++){
			int id = temp.get(i).getId()-1;
			if(i!=id)
				System.out.println(i + " : " + id);
		}
		*/
		
	}
		

	
}
