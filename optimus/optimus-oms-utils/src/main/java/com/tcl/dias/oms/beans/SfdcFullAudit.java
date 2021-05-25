package com.tcl.dias.oms.beans;

import java.util.Date;

public class SfdcFullAudit {

	private Integer id;
	private String request;
	private String response;
	private String updatedBy;
	private Date updatedTime;
	private String status;

	public SfdcFullAudit() {
		// DO NOTHING
	}

	public SfdcFullAudit(Integer id, String request, String response, String updatedBy, Date updatedTime,
			String status) {
		this.id = id;
		this.request = request;
		this.response = response;
		this.updatedBy = updatedBy;
		this.updatedTime = updatedTime;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "SfdcFullAudit [id=" + id + ", request=" + request + ", response=" + response + ", updatedBy="
				+ updatedBy + ", updatedTime=" + updatedTime + "]";
	}

}
