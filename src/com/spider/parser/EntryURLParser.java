package com.spider.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.spider.utils.URLQueue;

/**
 * 从分类中获取具体词条URL的解析器
 * @author hxuhao
 *
 */

public class EntryURLParser extends Parser implements Runnable{
	
	private LinkedList<String> queue = new LinkedList<String>();
	
	private LinkedList<String> entryURLs = new LinkedList<String>();
	public EntryURLParser(LinkedList queue,LinkedList<String> list){
		this.queue = queue;
		this.entryURLs = list;
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String url="";
		while(true){
			synchronized (queue) {
				if(!queue.isEmpty()){
					url = queue.poll();
						System.out.println("go : " + url);
				}else{
					break;
				}
			}

			String src = this.getSrc("http://fenlei.baike.com/" + url + "list");
			String regex = "<a[^>]+?href=\"http://www.baike.com/wiki/(?<subhref>[^\"]+)\"[^>?]*>(?<name>[^<]+)</a>";
			List<String> entryURL = this.selectTarget(src, regex);
			synchronized (entryURLs) {
				entryURLs.addAll(entryURL);
				System.out.println(entryURLs.size());
			}

		}

	}

}
