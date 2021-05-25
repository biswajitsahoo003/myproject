package com.tcl.dias.servicefulfillmentutils.beans;

import java.sql.Timestamp;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

/**
 * @author vivek
 *
 */
public class TaskRemarksRequest extends BaseRequest {

	private String userComments;
	private String username;
	private String isJeopardy;
	private Integer taskId;

	private Integer serviceId;

	private String jeopardyCategory ;
	private String currentJeopardyOwner;
	private Timestamp targetedCompletionDate;

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getUserComments() {
		return userComments;
	}

	public void setUserComments(String userComments) {
		this.userComments = userComments;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIsJeopardy() {
		return isJeopardy;
	}

	public void setIsJeopardy(String isJeopardy) {
		this.isJeopardy = isJeopardy;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getJeopardyCategory() {
		return jeopardyCategory;
	}

	public void setJeopardyCategory(String jeopardyCategory) {
		this.jeopardyCategory = jeopardyCategory;
	}

	public String getCurrentJeopardyOwner() {
		return currentJeopardyOwner;
	}

	public void setCurrentJeopardyOwner(String currentJeopardyOwner) {
		this.currentJeopardyOwner = currentJeopardyOwner;
	}

	public Timestamp getTargetedCompletionDate() {
		return targetedCompletionDate;
	}

	public void setTargetedCompletionDate(Timestamp targetedCompletionDate) {
		this.targetedCompletionDate = targetedCompletionDate;
	}
}
