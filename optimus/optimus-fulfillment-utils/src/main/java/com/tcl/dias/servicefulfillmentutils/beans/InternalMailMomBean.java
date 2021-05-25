package com.tcl.dias.servicefulfillmentutils.beans;

public class InternalMailMomBean {
	private String serialNo;
	private String id;
	private String items;
	private String responsibility;
	private String dueDate;
	private String status;
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
	}	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getResponsibility() {
		return responsibility;
	}
	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}


}
