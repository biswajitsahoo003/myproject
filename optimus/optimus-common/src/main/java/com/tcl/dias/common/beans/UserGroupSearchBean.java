package com.tcl.dias.common.beans;

import java.io.Serializable;

public class UserGroupSearchBean implements Serializable{
	
	private String name;
	
	private Integer page;
	
	private Integer size;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
	
	

}
