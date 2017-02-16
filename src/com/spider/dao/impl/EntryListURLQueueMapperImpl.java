package com.spider.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.spider.dao.EntryListURLQueueMapper;
import com.spider.model.EntryListURLQueue;


@Repository
@Transactional
public class EntryListURLQueueMapperImpl implements EntryListURLQueueMapper{
	
	private static Logger logger = LogManager.getLogger("EntryListURLQueueMapperImpl");
	
	@Resource(name="sqlSessionTemplate")
	private SqlSessionTemplate sqlSession;

	@Override
	public int insertSelective(EntryListURLQueue record) {
		// TODO Auto-generated method stub
		int ret =  sqlSession.insert("com.spider.dao.EntryListURLQueueMapper.insertSelective", record);
		//System.out.println("insert " + ret);
		return ret;
	}

	@Override
	public EntryListURLQueue selectOne() {
		// TODO Auto-generated method stub
		EntryListURLQueue url = sqlSession.selectOne("com.spider.dao.EntryListURLQueueMapper.selectOne");
		if(url != null)
			sqlSession.update("com.spider.dao.EntryListURLQueueMapper.updateStatus",url.getId());
		return url;
	}

	
	@Override
	public int updateStatus(Integer id) {
		// TODO Auto-generated method stub
		return sqlSession.update("com.spider.dao.EntryListURLQueueMapper.updateStatus",id);
	}

	@Override
	public int insertURLBatch(List<EntryListURLQueue> urls) {
		// TODO Auto-generated method stub
		/*
		for(EntryListURLQueue url : urls){
			System.out.println(url.getListUrl());
		}*/
		return sqlSession.insert("com.spider.dao.EntryListURLQueueMapper.insertURLBatch",urls);
	}
	

	@Override
	public List<EntryListURLQueue> selectBatch() {
		// TODO Auto-generated method stub
		List<EntryListURLQueue> queue = sqlSession.selectList("com.spider.dao.EntryListURLQueueMapper.selectBatch");
		if(queue.size()>0)
			sqlSession.update("com.spider.dao.EntryListURLQueueMapper.updateBatch", queue);
		System.out.println(queue.size());
		return queue;
	}
	
	public static void main(String[] args){
		// 测试
		ApplicationContext ac1 = new ClassPathXmlApplicationContext(new String[] {"spring-mybatis.xml"});
		for(String name : ac1.getBeanDefinitionNames()){
			System.out.println(name);
		}
		
//		logger.error("get failed");
//		logger.error("get failed");
		
		/*
		EntryListURLQueueMapper mapper = (EntryListURLQueueMapper) ac1.getBean("entryListURLQueueMapperImpl");
		EntryListURLQueue url = new EntryListURLQueue();
		EntryListURLQueue url2 = new EntryListURLQueue();
		EntryListURLQueue url3 = new EntryListURLQueue();
		url.setListUrl("asassasasasq");
		url2.setListUrl("233233233");
		url3.setListUrl("2332332334");
		List<EntryListURLQueue> urls = new ArrayList<EntryListURLQueue>();
		urls.add(url);
		urls.add(url2);
		urls.add(url3);

		mapper.insertURLBatch(urls);
		*/
		/*
		List<URLQueue> temp = mapper.selectMany();
		for(int i=0;i<temp.size();i++){
			int id = temp.get(i).getId()-1;
			if(i!=id)
				System.out.println(i + " : " + id);
		}
		*/
		
	}

	@Override
	public int updateByPrimaryKeySelective(EntryListURLQueue record) {
		// TODO Auto-generated method stub
		return sqlSession.update("com.spider.dao.EntryListURLQueueMapper.updateByPrimaryKeySelective", record);
	}
	
}
