package com.spider.parser;




import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.spider.client.HttpConnectionManager;
import com.spider.model.MyEntryInfo;
import com.spider.model.URLQueue;


/**
 * 词条内容解析器
 * @author hxuhao
 *
 */
@Component
public class EntryContentParser extends Parser implements Runnable{
	
	private static Logger logger = LogManager.getLogger("EntryListURLParser");
	
	private BlockingQueue urlQueue;
	
	private BlockingQueue<MyEntryInfo> entryQueue;
	
	@Resource(name="httpConnectionManager")
	HttpConnectionManager connectionManager;
	
	public BlockingQueue getUrlQueue() {
		return urlQueue;
	}

	public void setUrlQueue(BlockingQueue url) {
		this.urlQueue = url;
	}
	
	public BlockingQueue<MyEntryInfo> getEntryQueue() {
		return entryQueue;
	}

	public void setEntryQueue(BlockingQueue<MyEntryInfo> entryQueue) {
		this.entryQueue = entryQueue;
	}



	// 去除<a>和</a>
	public String clear(String src){
		StringBuilder sb = new StringBuilder(src);
		Pattern pattern = Pattern.compile("<a[^>]*>|</a>");
		Matcher matcher = pattern.matcher(sb.toString());

		while(matcher.find()){
			//System.out.println(matcher.group(0) + " : " + matcher.start() + " : " + matcher.end());
			StringBuilder block = new StringBuilder();
			for(int i=0;i<matcher.end() - matcher.start();i++)
				block.append(" ");
			sb.replace(matcher.start(), matcher.end(),block.toString());
			//System.out.println(sb.length());
		}

		return sb.toString().replace(" ", "");

	}
	
	// 获取词条具体内容
	public MyEntryInfo getEntryContent(String url){
		if(url==null || url.trim().equals(""))
			return null;
    	url =  url.replaceAll("&", "%26");
    	url =  url.replaceAll(" ", "%20");
		url = "http://www.baike.com/wiki/"+url;
		
		String src = connectionManager.getHtml(url);
		if(src==null){
			return null;
		}
		StringBuilder sb = new StringBuilder(src);
		
		// 获取词条名称
		String nameRegex = "<h1>(?<name>[^<]+)</h1>";
		List<String> names = this.selectTarget(src.toString(), nameRegex);
		
		// 获取开放分类
		String labelRegex = "<a[^>]+?title=\"(?<label>[^\"]+)\"[^>]*?href=\"http://fenlei.baike.com/(?<subhref>[^\"]+)\"[^>?]*>[^<]+</a>";
		List<String> labels = this.selectTarget(src.toString(), labelRegex);
		
		// 获取图片链接
		String imageRegex = "<div[^>]+class=\"doc-img\"[^>]*>[\n]*<a[^>]+>[\n]*<img[^>]+src=\"(?<url>[^\"]+)\"";
		List<String> imageURL = this.selectTarget(src.toString(), imageRegex);
	
		
		// 获取摘要
		String summaryRegex = "<div class=\"summary\"><p>(?<summary>.*)</p>";
		List<String> summary = this.selectTarget(sb.toString(), summaryRegex);

		String name = names.size()>0?names.get(0):"unknown" + UUID.randomUUID().toString();
		String content = summary.size()>0?summary.get(0):"暂无内容";
		content = this.clear(content);
		
		String imageSrc = imageURL.size()>0?imageURL.get(0):"defaultEntryPic.jpg";
		//String label1 = labels.size()>0?labels.get(0):"未知标签";
		

		MyEntryInfo entry = new MyEntryInfo();
		entry.setEntryName(name);
		entry.setEntryContent(content);
		entry.setPictureAddr(imageSrc);
		entry.setCreateName("互动百科");
		entry.setLabels(labels);
		/*
		Entry entry = new Entry();
	
		entry.setName(name);
		entry.setLabel(labels);

		entry.setContent(content);
		entry.setImageSrc(imageSrc);*/
		return entry;
		//System.out.println(src);
	}

	@Override
	public void run() {
		String url="";
		URLQueue q = null;
		while(true){
			try {
				q = (URLQueue) urlQueue.poll(5, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(q==null)
				break;
			url = q.getUrl();
			
		  	// url转换
			url = url.replaceAll("&", "%26");
			url = url.replaceAll(" ", "%20");

			MyEntryInfo entryInfo = getEntryContent(url);
			if(entryInfo==null){
				logger.error("get id : "+ q.getId() + " url: " + url + "failed");
				continue;
			}
		//	System.out.println(Thread.currentThread().getId() + "get id : "+ q.getId());
			try {
				entryQueue.put(entryInfo);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			
		}
		System.out.println(Thread.currentThread().getId() + " fin");
		logger.info(Thread.currentThread().getId() + " fin");
	}
	
	
	public static void main(String[] args){
		
		
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("spring-mybatis.xml");
		EntryContentParser parser = (EntryContentParser) ac.getBean("entryContentParser");
		URLQueue u = new URLQueue();
		u.setUrl("GPRS");
		BlockingQueue<Object> q = new ArrayBlockingQueue<>(10);
		q.add(u);
		parser.setUrlQueue(q);
		parser.run();
		
	}
}
