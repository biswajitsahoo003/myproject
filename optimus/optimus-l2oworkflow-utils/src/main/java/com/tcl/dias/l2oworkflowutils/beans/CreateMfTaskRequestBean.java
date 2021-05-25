package com.tcl.dias.l2oworkflowutils.beans;

import java.io.Serializable;
import java.util.List;

public class CreateMfTaskRequestBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4263344323312563444L;
	
	
	private Integer requestorTaskId;
	private List<MfTaskRequestBean> taskRequest;
	
	
	public List<MfTaskRequestBean> getTaskRequest() {
		return taskRequest;
	}
	public void setTaskRequest(List<MfTaskRequestBean> taskRequest) {
		this.taskRequest = taskRequest;
	}
	public Integer getRequestorTaskId() {
		return requestorTaskId;
	}
	public void setRequestorTaskId(Integer requestorTaskId) {
		this.requestorTaskId = requestorTaskId;
	}
	
		
}
