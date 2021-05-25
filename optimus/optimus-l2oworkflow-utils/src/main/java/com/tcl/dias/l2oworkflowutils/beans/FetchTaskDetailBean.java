package com.tcl.dias.l2oworkflowutils.beans;

import java.util.List;

public class FetchTaskDetailBean {
	
	private Integer siteId;
	
	private String siteCode;
		
	private List<TaskDetailsBean> taskList;

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public List<TaskDetailsBean> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<TaskDetailsBean> taskList) {
		this.taskList = taskList;
	}
	
	
	
	
	

}
