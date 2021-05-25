package com.tcl.dias.l2oworkflowutils.beans;

public class TaskDetailsBean {
	
	private String taskDefKey;
	private Integer taskId; 
	private String feasibilityId;
	private Integer mfDetailId;
	private String userName;
	private String groupName;
	private String status;
	
	
	public String getTaskDefKey() {
		return taskDefKey;
	}
	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}

	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public String getFeasibilityId() {
		return feasibilityId;
	}
	public void setFeasibilityId(String feasibilityId) {
		this.feasibilityId = feasibilityId;
	}
	public Integer getMfDetailId() {
		return mfDetailId;
	}
	public void setMfDetailId(Integer mfDetailId) {
		this.mfDetailId = mfDetailId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	

}
