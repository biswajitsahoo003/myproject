package com.tcl.dias.common.beans;

public class CommercialTaskDetailsBean {
	private String quoteCode;
	private String quoteId;
	private String taskId;
	private String tcv;
	private String approverName;
	private String approverLevel;

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
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTcv() {
		return tcv;
	}
	public void setTcv(String tcv) {
		this.tcv = tcv;
	}
	public String getApproverName() {
		return approverName;
	}
	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}
	public String getApproverLevel() { return approverLevel; }
	public void setApproverLevel(String approverLevel) { this.approverLevel = approverLevel; }


}
