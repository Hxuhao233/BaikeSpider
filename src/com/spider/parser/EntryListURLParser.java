package com.spider.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.spider.Spider;
import com.spider.client.HttpConnectionManager;
import com.spider.dao.EntryListURLQueueMapper;
import com.spider.dao.impl.EntryListURLQueueMapperImpl;
import com.spider.model.EntryListURLQueue;
import com.spider.utils.JsonUtils;

/**
 * 词条列表URL解析器
 * @author hxuhao
 *
 */
@Component
public class EntryListURLParser extends Parser implements Runnable{

	private static Logger logger = LogManager.getLogger("EntryListURLParser");
	
	@Resource(name="httpConnectionManager")
	HttpConnectionManager connectionManager;
	
	@Resource(name="entryListURLQueueMapperImpl")
	private EntryListURLQueueMapper listMapper;
	
	public void insert(List<EntryListURLQueue>urls){
		listMapper.insertURLBatch(urls);
	}
	
	private String NORMAL_CATEGORY_URL;
	
	private String HOT_CATEGORY_URL_PREFIX;
	private String HOT_CATEGORY_URL_POSTFIX;
	
	private String CATEGORY_TREE_URL;
	private String CHILD_CATEGORY_URL;
	
	
	@Override
	public void run() {
		ArrayList<String> listURLs =  new ArrayList<String>();
		ArrayList<EntryListURLQueue> entryListURLQueue = new ArrayList<EntryListURLQueue>();
		
		Spider spider = new Spider();
		
		long start = System.currentTimeMillis();
		
		// 获得普通分类
		String src = connectionManager.getHtml("http://www.baike.com/fenlei/");//\<a.*href=.*\>.*\</a\>
		listURLs.addAll(spider.selectTarget(src, "<a[^>]+?href=\"http://fenlei.baike.com/(?<subhref>[^\"prd]+)\"[^>?]*>(?<name>[^<]+)</a>"));
	
		
		
		// 获得热门分类
		for(int i=1;i<=4;i++){
			String src2 = spider.getSrc("http://www.baike.com/cmsMorePage.do?page_now=" + i + "&templateId=100000030042");
			
			listURLs.addAll(spider.selectTarget(src2,  "(<a[^>]+?href=\"http://fenlei.baike.com/(?<subhref>[^\"]+)\"[^>?]*>(?<name>[^<]+)</a>" + 
		 "| <a[^>]+?href=\"http://www.baike.com/categorypage/show/\\k<subhref>[^\"]+\"[^>?]*>\\k<name>[^<]+</a>)"));
		}
		


		ArrayList<String> categorys = new ArrayList<String>();
		//ArrayList<String> result = new ArrayList<String>();
		categorys.add("页面总分类");
		
		int i=0;
		while(i<categorys.size()){
			String currentCategorys = categorys.get(i);
			
			System.out.println("in : " + currentCategorys);
			String childCategorysJson = connectionManager.getHtml("http://www.baike.com/category/Ajax_cate.jsp?catename=" + currentCategorys);
			System.out.println(childCategorysJson);
			//result.add(currentCategorys);
			ArrayList<HashMap<String,Object>>  jsonObject = JsonUtils.decode(childCategorysJson, ArrayList.class);
			if(jsonObject!=null && jsonObject.size()>0){
				for(Map<String,Object> map : jsonObject){
					String v = (String) map.get("name");

					if(!v.trim().equals("") && !categorys.contains(v)){
						System.out.println("add : " + v);
						categorys.add(v);
					}
				}
			}else{
				System.out.println("none child category");
			}
			i++;
			System.out.println("i : " + i);
			System.out.println("total : " + categorys.size());
		}

		for(String url : listURLs){
			if(url.toCharArray()[url.length()-1]!='/')
				url += "/";
			EntryListURLQueue listURL = new EntryListURLQueue();
			listURL.setListUrl(url);
			//System.out.println(url);
			entryListURLQueue.add(listURL);
		}
		for(String url : categorys){
			if(url.toCharArray()[url.length()-1]!='/')
				url += "/";
			EntryListURLQueue listURL = new EntryListURLQueue();
			listURL.setListUrl(url);
			//System.out.println(url);
			entryListURLQueue.add(listURL);
		}

		long end = System.currentTimeMillis();
		System.out.println(entryListURLQueue.size());
		System.out.println(end - start + " ms");
		
		insert(entryListURLQueue);
		
	}
		
	
	
	public static void main(String[] args){
		ApplicationContext ac = new ClassPathXmlApplicationContext(new String[] {"spring-mybatis.xml"});
		HttpConnectionManager connectionManager = (HttpConnectionManager) ac.getBean("httpConnectionManager");
		EntryListURLQueueMapper listURLQueueMapper = (EntryListURLQueueMapper) ac.getBean("entryListURLQueueMapperImpl");
		
		ArrayList<String> listURLs =  new ArrayList<String>();
		ArrayList<EntryListURLQueue> entryListURLQueue = new ArrayList<EntryListURLQueue>();

		ArrayList<String> categorys = new ArrayList<String>();
		
		Spider spider = new Spider();
		
		long start = System.currentTimeMillis();
		/*
		// 获得普通分类
		String src = connectionManager.getHtml("http://www.baike.com/fenlei/");//\<a.*href=.*\>.*\</a\>
		listURLs.addAll(spider.selectTarget(src, "<a[^>]+?href=\"http://fenlei.baike.com/(?<subhref>[^\"prd]+)\"[^>?]*>(?<name>[^<]+)</a>"));
		*/
		
		
		// 获得热门分类
		for(int i=1;i<=4;i++){
			String src2 = spider.getSrc("http://www.baike.com/cmsMorePage.do?page_now=" + i + "&templateId=100000030042");
			
			listURLs.addAll(spider.selectTarget(src2,  "<a[^>]+?href=\"http://fenlei.baike.com/(?<subhref>[^\"]+)\"[^>?]*>(?<name>[^<]+)</a>"));
			listURLs.addAll(spider.selectTarget(src2,"<a[^>]+?href=\"http://www.baike.com/categorypage/show/(?<subhref>[^\"]+)\"[^>?]*>(?<name>[^<]+)</a>"));
			System.out.println(listURLs.size());
		}
		for(String url : listURLs){
			if(url.toCharArray()[url.length()-1]!='/')
				url += "/";
			EntryListURLQueue listURL = new EntryListURLQueue();
			listURL.setListUrl(url);
			System.out.println(url);
			entryListURLQueue.add(listURL);
		}
		System.out.println(entryListURLQueue.size());

		/*
		//ArrayList<String> result = new ArrayList<String>();
		categorys.add("页面总分类");
		
		int i=0;
		while(i<categorys.size()){
			String currentCategorys = categorys.get(i);
			currentCategorys = currentCategorys.replaceAll(" ", "%20");
			System.out.println("in : " + currentCategorys);
			String childCategorysJson = connectionManager.getHtml("http://www.baike.com/category/Ajax_cate.jsp?catename=" + currentCategorys);
			System.out.println(childCategorysJson);
			//result.add(currentCategorys);
			ArrayList<HashMap<String,Object>>  jsonObject = JsonUtils.decode(childCategorysJson, ArrayList.class);
			if(jsonObject!=null && jsonObject.size()>0){
				for(Map<String,Object> map : jsonObject){
					String v = (String) map.get("name");

					if(!v.trim().equals("") && !categorys.contains(v)){
						System.out.println("add : " + v);
						categorys.add(v);
					}
				}
			}else{
				System.out.println("none child category");
			}
			i++;
			System.out.println("i : " + i);
			System.out.println("total : " + categorys.size());
		}


		for(String url : categorys){
			if(url.toCharArray()[url.length()-1]!='/')
				url += "/";
			EntryListURLQueue listURL = new EntryListURLQueue();
			listURL.setListUrl(url);
			//System.out.println(url);
			entryListURLQueue.add(listURL);
		}

		long end = System.currentTimeMillis();
		System.out.println(entryListURLQueue.size());
		System.out.println(end - start + " ms");
				*/
		listURLQueueMapper.insertURLBatch(entryListURLQueue);
		

	
	}

}
