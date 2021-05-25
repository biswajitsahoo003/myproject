package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.ArrayList;
import java.util.List;

public class TaskResponse {

	private List<TaskBean> delayTasks;

	public List<TaskBean> getDelayTasks() {
		
		if(delayTasks==null) {
			delayTasks=new ArrayList<>();
		}
		return delayTasks;
	}

	public void setDelayTasks(List<TaskBean> delayTasks) {
		this.delayTasks = delayTasks;
	}

	

}
