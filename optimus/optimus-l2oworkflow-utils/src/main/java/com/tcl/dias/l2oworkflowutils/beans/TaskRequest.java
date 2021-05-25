package com.tcl.dias.l2oworkflowutils.beans;

import java.util.List;

public class TaskRequest {

	private String userName;

	private Integer serviceId;

	private List<String> status;
	private List<String> serviceType;

	private List<String> orderType;

	private List<String> taskName;
	
	private List<String> city;

	private String serviceCode;

	private String groupBy;

	private String groupName;
	
	private String searchText;
	
	private String createdTimeFrom;
	
	private String createdTimeTo;
	
	private String wfName;   // Added for manual feasibility
	
    private String owner;
	
    private String type;
    
    private String productName;
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getOwner() {
		return owner;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getServiceType() {
		return serviceType;
	}

	public void setServiceType(List<String> serviceType) {
		this.serviceType = serviceType;
	}

	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}

	public List<String> getOrderType() {
		return orderType;
	}

	public void setOrderType(List<String> orderType) {
		this.orderType = orderType;
	}

	public List<String> getTaskName() {
		return taskName;
	}

	public void setTaskName(List<String> taskName) {
		this.taskName = taskName;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public List<String> getCity() {
		return city;
	}

	public void setCity(List<String> city) {
		this.city = city;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getCreatedTimeFrom() {
		return createdTimeFrom;
	}

	public void setCreatedTimeFrom(String createdTimeFrom) {
		this.createdTimeFrom = createdTimeFrom;
	}

	public String getCreatedTimeTo() {
		return createdTimeTo;
	}

	public void setCreatedTimeTo(String createdTimeTo) {
		this.createdTimeTo = createdTimeTo;
	}

	public String getWfName() {
		return wfName;
	}

	public void setWfName(String wfName) {
		this.wfName = wfName;
	}
	

}
