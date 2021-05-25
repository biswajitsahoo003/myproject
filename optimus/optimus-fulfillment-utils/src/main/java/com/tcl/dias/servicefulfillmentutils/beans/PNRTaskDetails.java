package com.tcl.dias.servicefulfillmentutils.beans;

import java.sql.Timestamp;

public class PNRTaskDetails {

	private Timestamp createdTime;
	private Timestamp completedTime;
	private String taskDefKey;
	private String name;
	private String status;
	
	private String siteType;
	
	private String groupName;
	
	

	
	
	
	

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the siteType
	 */
	public String getSiteType() {
		return siteType;
	}

	/**
	 * @param siteType the siteType to set
	 */
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Timestamp getCompletedTime() {
		return completedTime;
	}

	public void setCompletedTime(Timestamp completedTime) {
		this.completedTime = completedTime;
	}

	public String getTaskDefKey() {
		return taskDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
