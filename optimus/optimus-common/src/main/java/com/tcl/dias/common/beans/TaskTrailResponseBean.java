package com.tcl.dias.common.beans;

import java.sql.Timestamp;

/**
 * This file contains the TaskTrailResponseBean.java class.
 * 
 *
 * @author Kruthika S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class TaskTrailResponseBean {

	private String feasibilityId;
	private Integer taskId;
	private String action;
	private String actionFrom;
	private String actionTo;
	private String groupFrom;
	private String groupTo;
	private Timestamp createdTime;
	private String quoteCode;
	private String quoteId;
	private String subject;
	private String requestorComment;
	private String responderComment;
	private String status;
	private String active;
	
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public Timestamp getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getActionFrom() {
		return actionFrom;
	}
	public void setActionFrom(String actionFrom) {
		this.actionFrom = actionFrom;
	}
	public String getActionTo() {
		return actionTo;
	}
	public void setActionTo(String actionTo) {
		this.actionTo = actionTo;
	}
	public String getGroupFrom() {
		return groupFrom;
	}
	public void setGroupFrom(String groupFrom) {
		this.groupFrom = groupFrom;
	}
	public String getGroupTo() {
		return groupTo;
	}
	public void setGroupTo(String groupTo) {
		this.groupTo = groupTo;
	}
	
	public String getQuoteCode() {
		return quoteCode;
	}
	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}
	public String getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getRequestorComment() {
		return requestorComment;
	}
	public void setRequestorComment(String requestorComment) {
		this.requestorComment = requestorComment;
	}
	public String getResponderComment() {
		return responderComment;
	}
	public void setResponderComment(String responderComment) {
		this.responderComment = responderComment;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFeasibilityId() {
		return feasibilityId;
	}
	public void setFeasibilityId(String feasibilityId) {
		this.feasibilityId = feasibilityId;
	}


}
