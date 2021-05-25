package com.tcl.dias.wfe.dto;

import java.util.List;

public class WfeDto {

	private int wfeId;
	private String name;
	private String desc;

	private List<String> sampleList;

	public int getWfeId() {
		return wfeId;
	}

	public void setWfeId(int wfeId) {
		this.wfeId = wfeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<String> getSampleList() {
		return sampleList;
	}

	public void setSampleList(List<String> sampleList) {
		this.sampleList = sampleList;
	}
}
