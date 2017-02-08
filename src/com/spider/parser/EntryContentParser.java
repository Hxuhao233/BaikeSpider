package com.spider.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.spider.model.Entry;
import com.spider.model.EntryInfo;
import com.spider.redis.RedisConnection;
import com.spider.redis.RedisManager;
import com.spider.utils.JsonUtils;

public class EntryContentParser extends Parser implements Runnable{
	
	private LinkedList< String> entryURLs;
	
	public EntryContentParser(LinkedList<String> entryurls) {
		this.entryURLs = entryurls;
	}
	
	
	// 预处理
	public String preHandle(String url){
		if(url==null || url.trim().equals(""))
			return null;
		url = "http://www.baike.com/wiki/"+url;
		StringBuilder sb = new StringBuilder(this.getSrc(url));
		this.clear(sb);
		return sb.toString();
	}
	
	// 去除网页中的所有<a></a>
	public void clear(StringBuilder sb){
		Pattern pattern = Pattern.compile("<a[^>]*>|</a>");
		Matcher matcher = pattern.matcher(sb.toString());

		while(matcher.find()){

			System.out.println(matcher.group(0) + " : " + matcher.start() + " : " + matcher.end());
			StringBuilder block = new StringBuilder();
			for(int i=0;i<matcher.end() - matcher.start();i++)
				block.append(" ");
			sb.replace(matcher.start(), matcher.end(),block.toString());
			System.out.println(sb.length());
		}

	}
	
	// 获取词条具体内容
	public EntryInfo getEntryContent(String url){
		if(url==null || url.trim().equals(""))
			return null;
		url = "http://www.baike.com/wiki/"+url;
		StringBuilder src = new StringBuilder(this.getSrc(url));
		// 获取词条名称
		String nameRegex = "<h1>(?<name>[^<]+)</h1>";
		List<String> names = this.selectTarget(src.toString(), nameRegex);
		
		// 获取开放分类
		String labelRegex = "<a[^>]+?title=\"(?<label>[^\"]+)\"[^>]*?href=\"http://fenlei.baike.com/(?<subhref>[^\"]+)\"[^>?]*>[^<]+</a>";
		List<String> labels = this.selectTarget(src.toString(), labelRegex);
		
	
		
		// 获取摘要
		this.clear(src);
		String summaryRegex = "<div class=\"summary\"><p>(?<summary>[^<]+)</p>";
		List<String> summary = this.selectTarget(src.toString(), summaryRegex);
		
		// 获取图片链接
		String imageRegex = "<div[^>]+class=\"doc-img\"[^>]*>[\n]*<a[^>]+>[\n]*<img[^>]+src=\"(?<url>[^\"]+)\"";
		List<String> imageURL = this.selectTarget(src.toString(), imageRegex);
		String name = names.size()>0?names.get(0):"unknown" + UUID.randomUUID().toString();
		String content = summary.size()>0?summary.get(0).replace(" ",""):"暂无内容";
		String imageSrc = imageURL.size()>0?imageURL.get(0):"defaultEntryPic.jpg";
		String label1 = labels.size()>0?labels.get(0):"未知标签";
		EntryInfo entry = new EntryInfo();
		entry.setEntryName(name);
		entry.setEntryContent(content);
		entry.setPictureAddr(imageSrc);
		entry.setCreateName("互动百科");
		entry.setLabel1(label1);
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
		while(true){
			synchronized (entryURLs) {
				if(!entryURLs.isEmpty()){
					url = entryURLs.poll();
					System.out.println(entryURLs.size());
				}else{
					break;
				}
			}
			EntryInfo e = this.getEntryContent(url);
			RedisConnection connection=null;
			try{
				connection = RedisManager.getInstance().getConnection();
				System.out.println("add : " + e.getEntryName());
				connection.hset(e.getEntryName(), "content", JsonUtils.encode(e));
			}finally{
				if(connection!=null)
					connection.close();
			}
		}
	}
	
	public static void main(String[] args){
		LinkedList<String> url = new LinkedList<>();
		url.add("GPRS");
		EntryContentParser parser = new EntryContentParser(url);
		EntryInfo e = parser.getEntryContent("GPRS");
		System.out.println(e.toString());
	
	}
}
