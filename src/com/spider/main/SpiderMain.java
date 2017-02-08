package com.spider.main;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import com.spider.Spider;
import com.spider.parser.EntryContentParser;
import com.spider.parser.EntryURLParser;
import com.spider.utils.URLQueue;

public class SpiderMain {
	public static void main(String[] args){
		ExecutorService threadPool = Executors.newCachedThreadPool();

		Spider spider = new Spider();
		long start = System.currentTimeMillis();
			
		// 1.从入口获得分类(只获取特定分类,所以需要排除一些<a>)
		String src = spider.getSrc("http://www.baike.com/fenlei/");//\<a.*href=.*\>.*\</a\>
		LinkedList<String> listURLs =  spider.selectTarget(src, "<a[^>class]+?href=\"http://fenlei.baike.com/(?<subhref>[^\"prd]+)\"[^>?]*>(?<name>[^<]+)</a>");
		System.out.println(listURLs.size());
		/*
		LinkedBlockingDeque<String> entryListURL = new LinkedBlockingDeque<>(listURLs);
		URLQueue queue = new URLQueue(entryListURL);
		*/
		LinkedList<String> entryURL = new LinkedList<String>();
		EntryURLParser entryURLParser = new EntryURLParser(listURLs, entryURL);
		threadPool.execute(entryURLParser);
		threadPool.shutdown();
        while(true){  
           if(threadPool.isTerminated()){  
        	   long end = System.currentTimeMillis();
        	   System.out.println(entryURL.size());
        	   System.out.println("time : " + (end - start) + " ms");
        	   System.out.println("获取词条URL完成");  
        	   break;  
            }  
        }
               
        threadPool = Executors.newFixedThreadPool(20);
        EntryContentParser entryContentParser = new EntryContentParser(entryURL);
        threadPool.execute(entryContentParser);
		threadPool.shutdown();
        while(true){  
            if(threadPool.isTerminated()){  
         	   long end2 = System.currentTimeMillis();
         	   System.out.println(entryURL.size());
         	   System.out.println("time : " + (end2 - start) + " ms");
         	   System.out.println("解析词条内容完成");  
         	   break;  
             }  
         }
        

	}
}
