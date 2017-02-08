package com.spider.utils;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.spider.dao.EntryMapper;
import com.spider.model.Entry;


/**
 * URL 队列
 * @author hxuhao
 *
 */
public class URLQueue {
	// 考虑多线程
	private BlockingQueue<String> unvisitedURL;
	private Set visitedURL = new HashSet();
	
	public URLQueue(BlockingQueue<String> urls){
		this.unvisitedURL = urls;
	}
	
	public void addUnvisitedURL(String url){
		if(url!=null && !url.trim().equals("") /*&& !visitedURL.contains(url)*/){
			unvisitedURL.add(url);
		}
	}
	public String getUnvisitedURL(){
	
		try {
			String url = unvisitedURL.take();
			return url;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public boolean isEmpty(){
		return unvisitedURL.isEmpty();
	}
	public Queue getUnVisitedURL() {
		return unvisitedURL;
	}
	public void setUnVisitedURL(BlockingQueue unVisitedURL) {
		this.unvisitedURL = unVisitedURL;
	}
	public Set getVisitedURL() {
		return visitedURL;
	}
	public void setVisitedURL(Set visitedURL) {
		this.visitedURL = visitedURL;
	}
	public static void main(String[] args){
		ApplicationContext ac1 = new ClassPathXmlApplicationContext(new String[] {"spring-mybatis.xml"});
		Entry e = new Entry();
		e.setContent("测试词条");
		e.setEntryname("测试词条");
		e.setPublisher("测试用户");
		EntryMapper mapper = (EntryMapper) ac1.getBean("entryMapper");
		mapper.insertSelective(e);
		
	}
	
	
}
