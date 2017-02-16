package com.spider.model;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import javafx.scene.input.DataFormat;


/**
 * 
 * 词条基本信息
 * @author hxuhao
 *
 */
public class EntryInfo implements Serializable{
	private String entryName;
	private String entryContent;
	private List<String> labels;
	private String createName;
	private String pictureAddr;
	private String eid;
	private String createDate;
	private Integer praiseTimes;
	private Integer badReviewTimes;

	
	public String getCreateDate() {
		return createDate;
	}


	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}


	public EntryInfo() {
		super();
	}


	public String getEntryName() {
		return entryName;
	}


	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}


	public String getEntryContent() {
		return entryContent;
	}


	public void setEntryContent(String entryContent) {
		this.entryContent = entryContent;
	}





	public List<String> getLabels() {
		return labels;
	}


	public void setLabels(List<String> labels) {
		this.labels = labels;
	}


	public String getCreateName() {
		return createName;
	}


	public void setCreateName(String createName) {
		this.createName = createName;
	}


	public String getPictureAddr() {
		return pictureAddr;
	}


	public void setPictureAddr(String pictureAddr) {
		this.pictureAddr = pictureAddr;
	}


	public String getEid() {
		return eid;
	}


	public void setEid(String eid) {
		this.eid = eid;
	}


	public Integer getPraiseTimes() {
		return praiseTimes;
	}


	public void setPraiseTimes(Integer praiseTimes) {
		this.praiseTimes = praiseTimes;
	}


	public Integer getBadReviewTimes() {
		return badReviewTimes;
	}


	public void setBadReviewTimes(Integer badReviewTimes) {
		this.badReviewTimes = badReviewTimes;
	}
	
	public String toString(){
		return "name : " + this.entryName + "\n"
						+ "label : " + Arrays.toString(this.labels.toArray()) + "\n"
						+ "content : " + this.entryContent + "\n"
						+ "imageSrc : " + this.pictureAddr + "\n";
	}
}
