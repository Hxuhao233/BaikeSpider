package com.spider.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
	// 获取网页源代码
	protected String getSrc(String url){
			
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
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
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
	protected List<String> selectTarget(String src,String patternStr){
		List<String> result = new ArrayList<>();
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
}
