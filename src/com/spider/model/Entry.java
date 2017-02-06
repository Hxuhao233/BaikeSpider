package com.spider.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Entry {
	private String name;
	private List<String> label;
	private String content;
	private String imageSrc;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getLabel() {
		return label;
	}
	public void setLabel(List<String> label) {
		this.label = label;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImageSrc() {
		return imageSrc;
	}
	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}
	
	public String toString(){
		return "name : " + this.name + "\n"
						+ "label : " + Arrays.toString(this.label.toArray()) + "\n"
						+ "content : " + this.content + "\n"
						+ "imageSrc : " + this.imageSrc + "\n";
	}
	
}
