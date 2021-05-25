package com.tcl.dias.servicefulfillmentutils.beans;

import java.sql.Timestamp;

/**
 * @author vivek
 *ActivityBean is used to get the activity details
 */
public class ActivityBean {

	private Timestamp completedTime;

	private Timestamp createdTime;

	private Timestamp duedate;

	private Byte status;

	private Timestamp updatedTime;

	private String wfActivityId;

	private String name;

	public Timestamp getCompletedTime() {
		return completedTime;
	}

	public void setCompletedTime(Timestamp completedTime) {
		this.completedTime = completedTime;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Timestamp getDuedate() {
		return duedate;
	}

	public void setDuedate(Timestamp duedate) {
		this.duedate = duedate;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getWfActivityId() {
		return wfActivityId;
	}

	public void setWfActivityId(String wfActivityId) {
		this.wfActivityId = wfActivityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
