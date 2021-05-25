package com.tcl.dias.l2oworkflowutils.beans;

import java.sql.Timestamp;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * used to map the process data
 *
 */
public class ProcessBean {

	private Timestamp completedTime;

	private Timestamp createdTime;

	private Timestamp duedate;
	
	private String status;
	
	


	private Timestamp updatedTime;

	private String wfProcInstId;

	private ActivityBean activity;

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


	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getWfProcInstId() {
		return wfProcInstId;
	}

	public void setWfProcInstId(String wfProcInstId) {
		this.wfProcInstId = wfProcInstId;
	}

	public ActivityBean getActivity() {
		return activity;
	}

	public void setActivity(ActivityBean activity) {
		this.activity = activity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	
}
