package com.spider.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.spider.dao.EntryLabelMapper;
import com.spider.dao.EntryMapper;
import com.spider.dao.LabelMapper;
import com.spider.model.Entry;
import com.spider.model.EntryLabel;
import com.spider.model.Label;
import com.spider.service.ISearchService;
import com.spider.utils.EntryInfo;

@Service
public class SearchServiceImpl implements ISearchService{
	
	@Resource(name = "labelMapperImpl")
	private LabelMapper labelMapper;


	@Resource(name="entryMapperImpl")
	private EntryMapper entryMapper;

	@Resource(name="entryLabelMapperImpl")
	private EntryLabelMapper entryLabelMapper;
	
	
	@Override
	public List<EntryInfo> search(String key) {
		// TODO Auto-generated method stub
		Map<String,Object> param = new HashMap<>();
		param.put("key", "机");
		param.put("page", 0);
		List<Entry> entrys = entryMapper.selectByName(param);
		List<EntryInfo> entryInfo = new ArrayList<>();
		for(Entry e : entrys){
			EntryInfo info = new EntryInfo();
			System.out.println("search : " + e.getEntryname());
			info.setEid(String.valueOf(e.getId()));
			info.setBadReviewTimes(0);
			info.setEntryContent(e.getContent());
			info.setEntryName(e.getEntryname());
			info.setPictureAddr(e.getPictureaddr());
			List<Integer> labelIDs = entryLabelMapper.selectLabelIdByEntryId(e.getId());
			List<String> labelNames = new ArrayList<>();
			for(Integer lid : labelIDs){
				Label label = labelMapper.selectByPrimaryKey(lid);
				labelNames.add(label.getLabelname());
			}
			System.out.println(labelNames.size());
			switch (labelNames.size()) {
			case 0:
				
				break;
				
			case 1:
				info.setLabel1(labelNames.get(0));
				
				break;
				
			case 2:
				info.setLabel1(labelNames.get(0));
				info.setLabel2(labelNames.get(1));

				break;
				
			case 3:
				info.setLabel1(labelNames.get(0));
				info.setLabel2(labelNames.get(1));
				info.setLabel3(labelNames.get(2));
				
				break;

			default:
				info.setLabel1(labelNames.get(0));
				info.setLabel2(labelNames.get(1));
				info.setLabel3(labelNames.get(2));
				info.setLabel4(labelNames.get(3));
				
				break;
			}
			entryInfo.add(info);
				
		}
		return entryInfo;
	}
	
	public static void main(String [] args) throws InterruptedException{
		// 开启远程服务
		ApplicationContext ac =  new ClassPathXmlApplicationContext(new String[] {"spring-mybatis.xml"});
		Object lock = new Object();  
        synchronized (lock) {  
            lock.wait();  
        }  
	}
	
}
