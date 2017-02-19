package com.spider.service;

import java.util.List;

import javax.annotation.Resource;

import com.spider.dao.EntryLabelMapper;
import com.spider.dao.EntryMapper;
import com.spider.dao.LabelMapper;
import com.spider.utils.EntryInfo;

public interface ISearchService {
	
	List<EntryInfo> search(String key);
}
