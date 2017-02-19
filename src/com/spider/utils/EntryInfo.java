package com.spider.utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;


/**
 * 
 * 词条基本信息
 * @author hxuhao
 *
 */
public class EntryInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String entryName;
	private String entryContent;
	private String label1;
	private String label2;
	private String label3;
	private String label4;
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


	public String getLabel1() {
		return label1;
	}


	public void setLabel1(String label1) {
		this.label1 = label1;
	}


	public String getLabel2() {
		return label2;
	}


	public void setLabel2(String label2) {
		this.label2 = label2;
	}


	public String getLabel3() {
		return label3;
	}


	public void setLabel3(String label3) {
		this.label3 = label3;
	}


	public String getLabel4() {
		return label4;
	}


	public void setLabel4(String label4) {
		this.label4 = label4;
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
						+ "label : " + this.label1 + this.label2 +  this.label3 + this.label4 + "\n"
						+ "content : " + this.entryContent + "\n"
						+ "imageSrc : " + this.pictureAddr + "\n";
	}
}
