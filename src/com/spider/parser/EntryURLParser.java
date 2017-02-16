package com.spider.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.spider.client.HttpConnectionManager;
import com.spider.dao.EntryListURLQueueMapper;
import com.spider.dao.URLQueueMapper;
import com.spider.model.Entry;
import com.spider.model.EntryListURLQueue;
import com.spider.model.URLQueue;


/**
 * 从列表中获取具体词条URL的解析器
 * @author hxuhao
 *
 */

@Component
public class EntryURLParser extends Parser implements Runnable{
	
	private static Logger logger = LogManager.getLogger("EntryURLParser");
	
	@Resource(name="URLQueueMapperImpl")
	private URLQueueMapper urlQueueMapper;
	
	@Resource(name="entryListURLQueueMapperImpl")
	private EntryListURLQueueMapper listURLMapper;
	
	@Resource(name="httpConnectionManager")
	HttpConnectionManager connectionManager;

	private BlockingQueue listURLqueue ;
	
	
	public BlockingQueue getListURLqueue() {
		return listURLqueue;
	}


	public void setListURLqueue(BlockingQueue queue) {
		this.listURLqueue = queue;
	}


	@Override
	public void run() {
		
		while(true){
			// 获得分类URL
			EntryListURLQueue q;
			try {
				q = (EntryListURLQueue) listURLqueue.poll(5, TimeUnit.MINUTES);
				if(q==null){
					break;
				}
				String ListURL= q.getListUrl();
				List<URLQueue> queue = new ArrayList<>();
				
			  	// url转换
				ListURL = ListURL.replaceAll("&", "%26");
				ListURL = ListURL.replaceAll(" ", "%20");
		
				if(ListURL.toCharArray()[ListURL.length()-1]!='/')
					ListURL += "/";
				
		  
				System.out.println(Thread.currentThread().getId() + " id : " + q.getId() /*+ " url : "+ ListURL */);
				 
				// 获取分类源网页
				String src = connectionManager.getHtml("http://fenlei.baike.com/" + ListURL + "list");
				if(src==null){
					logger.error(ListURL + " failed" );
					q.setStatus((byte)3);
					listURLMapper.updateByPrimaryKeySelective(q);
					continue ;
				}
				
				// 获取词条URL
				String regex = "<a[^>]+?href=\"http://www.baike.com/wiki/(?<subhref>[^\"]+)\"[^>?]*>(?<name>[^<]+)</a>";
				List<String> entryURL = this.selectTarget(src, regex);
				
				// 添加至URLQueue
				for(String entryitem : entryURL){
					URLQueue u = new URLQueue();
					u.setUrl(entryitem);
					queue.add(u);
					//System.out.println("get url : " + entryitem);
				}
				
				if(queue.size()>0){
					synchronized (urlQueueMapper) {
						urlQueueMapper.insertURLBatch(queue);
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
		System.out.println("Parser " + Thread.currentThread().getId() + " fin");

	}


}
