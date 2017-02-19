package com.spider.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.spider.dao.EntryLabelMapper;
import com.spider.dao.EntryListURLQueueMapper;
import com.spider.dao.EntryMapper;
import com.spider.dao.LabelMapper;
import com.spider.dao.URLQueueMapper;
import com.spider.model.Entry;
import com.spider.model.MyEntryInfo;
import com.spider.model.EntryLabel;
import com.spider.model.EntryListURLQueue;
import com.spider.model.Label;
import com.spider.model.URLQueue;
import com.spider.parser.EntryContentParser;



public class SpiderMain {
	
	static ApplicationContext ac = new ClassPathXmlApplicationContext(new String[] {"spring-mybatis.xml"});
	private static Logger logger = LogManager.getLogger("SpiderMain");
	
	@Component
	public static class EntryListURLGetter implements Runnable{
		private BlockingQueue<Object> queue;

		@Resource(name="entryListURLQueueMapperImpl")
		EntryListURLQueueMapper listURLMapper;
		
		public BlockingQueue<Object> getQueue() {
			return queue;
		}

		public void setQueue(BlockingQueue<Object> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			List<EntryListURLQueue> q = listURLMapper.selectBatch();
			int count = 0;
			while(q.size()>0){
				for(EntryListURLQueue url : q){
					try {
						queue.put(url);
						System.out.println("add " + url.getId() );
						count ++;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					}
				}
				q = listURLMapper.selectBatch();
			}
			logger.info(Thread.currentThread().getId() + "listurl getter fin     total : " + count );
			
		}
		
	}
	
	@Component
	public static class EntryURLGetter implements Runnable{
		private BlockingQueue<Object> queue;

		@Resource(name="URLQueueMapperImpl")
		URLQueueMapper URLMapper;
		
		public BlockingQueue<Object> getQueue() {
			return queue;
		}

		public void setQueue(BlockingQueue<Object> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			List<URLQueue> q = URLMapper.selectBatch();
			int count = 0;
			while(q.size()>0){
				for(URLQueue url : q){
					try {
						queue.put(url);
						System.out.println("get : " + url.getId());
						count ++;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					}
				}
				q = URLMapper.selectBatch();
			}
			logger.info(Thread.currentThread().getId() + " url getter fin     total : " + count );
		}
		
	}
		
	@Component
	public static class EntrySaver implements Runnable{
		
		private BlockingQueue<MyEntryInfo> entryQueue;
		
		public BlockingQueue<MyEntryInfo> getEntryQueue() {
			return entryQueue;
		}
	
		public void setEntryQueue(BlockingQueue<MyEntryInfo> entryQueue) {
			this.entryQueue = entryQueue;
		}
		
		@Resource(name = "labelMapperImpl")
		private LabelMapper labelMapper;
	
	
		@Resource(name="entryMapperImpl")
		private EntryMapper entryMapper;
	
		@Resource(name="entryLabelMapperImpl")
		private EntryLabelMapper entryLabelMapper;
		
		public void run() {
			MyEntryInfo entryInfo = null;
			while(true){
				try {
					entryInfo = (MyEntryInfo) entryQueue.poll(5, TimeUnit.MINUTES);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				if(entryInfo==null)
					break;
				Entry entry = new Entry();
				entry.setContent(entryInfo.getEntryContent());
				entry.setEntryname(entryInfo.getEntryName());
				entry.setPictureaddr(entryInfo.getPictureAddr());
				entry.setPublisher(entryInfo.getCreateName());
			
				List<Label> labels = new ArrayList<Label>();
		
		
				entryMapper.insertSelective(entry);
				if(entry.getId()==null){
					logger.error("add entry " + entry.getEntryname() + " failed");
					continue;
				}
				int eid = entry.getId();
				//System.out.println(eid);
				List<String> labelNames = entryInfo.getLabels();
				if(labelNames.size()>0){
					for(String item : labelNames){
						Label l = new Label();
						l.setLabelname(item);
						labels.add(l);
		
					}
					labelMapper.insertBatch(labels);
					
					List<EntryLabel> els = new ArrayList<EntryLabel>();
					List<Integer> labelIDs = labelMapper.selectIdBatch(labelNames);
					for(Integer item : labelIDs){
						EntryLabel el = new EntryLabel();
						el.setEntryId(eid);
						el.setLabelId(item);
						els.add(el);
					}
					entryLabelMapper.insertBatch(els);
					
				}
			}
		}
	}
	
	public static void main(String[] args){

		long start = System.currentTimeMillis();
		//ExecutorService threadPool = Executors.newFixedThreadPool(8);
		/*
		BlockingQueue<Object> queue = new ArrayBlockingQueue<>(500, true);
		
		//获取词条列表URL线程
		EntryListURLGetter getter = (EntryListURLGetter) ac.getBean("spiderMain.EntryListURLGetter");
	
		getter.setQueue(queue);
		threadPool.execute(getter);
		// 解析词条url线程
		for(int i=0;i<5;i++){
			EntryURLParser parser = (EntryURLParser) ac.getBean("entryURLParser");
			parser.setListURLqueue(queue);
			threadPool.execute(parser);
		}

		threadPool.shutdown();
    
		
		while(true){  
	       if(threadPool.isTerminated()){  
	    	   long end = System.currentTimeMillis();
	    	   System.out.println("time : " + (end - start) + " ms");
	    	   System.out.println("获取词条URL完成");  
	    	   break;  
	        }  
        }
		
*/
        BlockingQueue<Object> q = new ArrayBlockingQueue<>(800, true);
        BlockingQueue<MyEntryInfo> eQueue = new ArrayBlockingQueue<MyEntryInfo>(800,true);
        
        
		//获取词条URL线程
        EntryURLGetter egetter = (EntryURLGetter) ac.getBean("spiderMain.EntryURLGetter");
		egetter.setQueue(q);
    	Thread t1 = new Thread(egetter);
    	t1.start();
		//threadPool.execute(egetter);
		

    	//threadPool.execute(saver);
		
        // 解析词条具体内容线程
    	EntryContentParser[] entryContentParsers = new EntryContentParser[5];
    	for(int i=0;i<5;i++){
    		entryContentParsers[i] = (EntryContentParser) ac.getBean("entryContentParser");
    		entryContentParsers[i].setUrlQueue(q);
    		entryContentParsers[i].setEntryQueue(eQueue);
        	Thread t = new Thread(entryContentParsers[i]);
        	t.start();
    		//threadPool.execute(entryContentParser);
    	}
    	

    	// 存放词条线程
    	EntrySaver saver = (EntrySaver) ac.getBean("spiderMain.EntrySaver");
    	saver.setEntryQueue(eQueue);
    	Thread t = new Thread(saver);
    	t.start();
    	while(true){
    		if(!t.isAlive()){
    			long end2 = System.currentTimeMillis();
    			System.out.println("time : " + (end2 - start) + " ms");
    			System.out.println("解析词条内容完成");  
    		}
    	}
    	
		//threadPool.shutdown();

    	/*
        while(true){  
            if(threadPool.isTerminated()){  
         	   long end2 = System.currentTimeMillis();
         	   System.out.println("time : " + (end2 - start) + " ms");
         	   System.out.println("解析词条内容完成");  
         	   break;  
             }  
         }
        */
	}
}
