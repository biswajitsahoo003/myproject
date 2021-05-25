package com.tcl.dias.serviceinventory.beans;

import java.util.List;

/**
 * Bean for returning task details
 * 
 * @author Srinivasa Raghavan
 */
public class SdwanTaskDetailsBean {
	private Integer taskId;
	private String taskName;
	private String taskStatus;
	private Integer percentageCompletion;
	private String templateName;
	private List<String> cpes;
	private String taskStartTime;
	private String taskEndTime;

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public Integer getPercentageCompletion() {
		return percentageCompletion;
	}

	public void setPercentageCompletion(Integer percentageCompletion) {
		this.percentageCompletion = percentageCompletion;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public List<String> getCpes() {
		return cpes;
	}

	public void setCpes(List<String> cpes) {
		this.cpes = cpes;
	}

	public String getTaskStartTime() {
		return taskStartTime;
	}

	public void setTaskStartTime(String taskStartTime) {
		this.taskStartTime = taskStartTime;
	}

	public String getTaskEndTime() {
		return taskEndTime;
	}

	public void setTaskEndTime(String taskEndTime) {
		this.taskEndTime = taskEndTime;
	}
}
