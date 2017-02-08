package com.spider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.spider.model.Entry;

/**
 * 		
 * 百科入口 : http://www.baike.com/fenlei/ 	
 * 分类前缀 : http://fenlei.baike.com/(__)
 * 词条列表 : http://fenlei.baike.com/(__)/list 
 * 具体词条 : http://www.baike.com/wiki/(__)
 * @author hxuhao
 *
 */

public class Spider {

	public Spider() {
		// TODO Auto-generated constructor stub
	}
	
	public String getSrc(String url){
		
		BufferedReader reader = null;
		StringBuilder src = new StringBuilder();
		
		try {
			URL realUrl = new URL(url);
			URLConnection connection = realUrl.openConnection();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));

			String line = "";
			while( (line = reader.readLine())!=null ){
				src.append(line);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(reader!=null)
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}			
		return src.toString();
		
	}
	
	// 获取目标内容
	public LinkedList<String> selectTarget(String src,String patternStr){
		LinkedList<String> result = new LinkedList<>();
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(src);

		while(matcher.find()){
			/*
			System.out.print(matcher.group(0));
			for(int i=1;i<=matcher.groupCount();i++){
				System.out.print(" : " + matcher.group(i));
			}
			System.out.println();
			*/
			result.add(matcher.group(1));
		}
		return result;
	}
	
	// 获取该分类下的词条的链接
	public List<String> selectEntryURL(List<String> listURLs){
		List<String> EntryURLs = new ArrayList<>();
		int count = 0;
		for(String listURL : listURLs){
			// http://fenlei.baike.com/%E8%BF%90%E5%8A%A8%E4%BC%9A/list
			String src = this.getSrc("http://fenlei.baike.com/" + listURL + "list");
			String regex = "<a[^>]+?href=\"http://www.baike.com/wiki/(?<subhref>[^\"]+)\"[^>?]*>(?<name>[^<]+)</a>";
			List<String> entryURLs = this.selectTarget(src, regex);
			count += entryURLs.size();
			EntryURLs.addAll(entryURLs);
			System.out.println(count);
		}
		System.out.println(count);
		return EntryURLs;
	}
	
	// 获取词条具体内容
	public Entry getEntryContent(String url){
		url = "奥格";
		String src = this.getSrc("http://www.baike.com/wiki/"+url);
		
		// 获取词条名称
		String nameRegex = "<h1>(?<name>[^<]+)</h1>";
		List<String> name = this.selectTarget(src, nameRegex);
		
		// 获取开放分类
		String labelRegex = "<a[^>]+?title=\"(?<label>[^\"]+)\"[^>]*?href=\"http://fenlei.baike.com/(?<subhref>[^\"]+)\"[^>?]*>[^<]+</a>";
		List<String> labels = this.selectTarget(src, labelRegex);
	
		// 获取摘要
		String summaryRegex = "<div class=\"summary\"><p>(?<summary>[^<]+)</p>";
		List<String> summary = this.selectTarget(src, summaryRegex);
		
		// 获取图片链接
		//<div class="img img_r" style="width:294px;">
		//<a title="多址接入技术" href="http://tupian.baike.com/a1_41_02_01300001220069147626025266656_jpg.html" target="_blank">
		//<img title="多址接入技术" alt="多址接入技术"  src="http://www.huimg.cn/lib/0.gif" data-original="http://a1.att.hudong.com/41/02/01300001220069147626025266656_s.jpg" /></a>
		// <div[^>]+class="[^"]+"[^>]+><a[^>]+><img[^>]+src="[^"]+?"
		String imageRegex = "<div[^>]+class=\"doc-img\"[^>]*>[\n]*<a[^>]+>[\n]*<img[^>]+src=\"(?<url>[^\"]+)\"";
		List<String> imageURL = this.selectTarget(src, imageRegex);
		
		Entry entry = new Entry();
		/*
		entry.setEntryname(name.get(0));
		entry.setLabel(labels);
		String content = summary.size()>0?summary.get(0):"暂无内容";
		String imageSrc = imageURL.size()>0?imageURL.get(0):null;
		entry.setContent(content);
		entry.setImageSrc(imageSrc);
		*/
		return entry;
		
		//System.out.println(src);
	}
	
	
	public static void main(String[] args){
		Spider spider = new Spider();
		long start = System.currentTimeMillis();
			
		// 1.从入口获得分类(只获取特定分类,所以需要排除一些<a>)
		String src = spider.getSrc("http://www.baike.com/fenlei/");//\<a.*href=.*\>.*\</a\>
		List<String> listURLs =  spider.selectTarget(src, "<a[^>class]+?href=\"http://fenlei.baike.com/(?<subhref>[^\"prd]+)\"[^>?]*>(?<name>[^<]+)</a>");
		System.out.println(listURLs.size());
		/*for(String item : result.keySet()){
			System.out.println(item + " : " + result.get(item));
		}
		System.out.println("==================================");
		*/
		// 2.获取分类下的词条列表
		List<String> list1 = new ArrayList<>();
		//list1.add(listURLs.get(0));
		List<String> entryURLs = spider.selectEntryURL(listURLs);
		
		long end = System.currentTimeMillis();
		System.out.println("time : " + (end - start) + " ms");
		System.out.println(entryURLs.size());
		/*
		// 3.获取具体词条
		String url = entryURLMap.get(list1.get(0)).get(0);
		Entry e = spider.getEntryContent(url);
		
		System.out.println(e.toString());
		*/
		
	}

}