package com.tcl.dias.oms.bulkfeasibility.beans;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
 * 
 * Bean to hold the status of the uploaded excel.
 * @author krutsrin
 *
 */
@JsonInclude(Include.NON_NULL)
public class StatusInfoResponseBean {
	private String msgFromDownStream;
	private String errorFromDownStream;
	private Integer jobId;
	private ArrayList<String> logMessages;
	private String status;
	private String userId;
	private String error;

	public Integer getJobId() {
		return jobId;
	}
	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}
	public ArrayList<String> getLogMessages() {
		return logMessages;
	}
	public void setLogMessages(ArrayList<String> logMessages) {
		this.logMessages = logMessages;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getMsgFromDownStream() {
		return msgFromDownStream;
	}
	public void setMsgFromDownStream(String msgFromDownStream) {
		this.msgFromDownStream = msgFromDownStream;
	}
	public String getErrorFromDownStream() {
		return errorFromDownStream;
	}
	public void setErrorFromDownStream(String errorFromDownStream) {
		this.errorFromDownStream = errorFromDownStream;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

}
