package com.tcl.dias.common.beans;

import java.io.Serializable;

public class ServiceScheduleBean implements Serializable{
	
	private String productName;
	
	private Boolean isSSUploaded;
	
	private String displayName;
	
	private String name;
	
	private Integer customerLeId;

	private String uriPath;

	public Integer getCustomerLeId() {
		return customerLeId;
	}

	public void setCustomerLeId(Integer customerLeId) {
		this.customerLeId = customerLeId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Boolean getIsSSUploaded() {
		return isSSUploaded;
	}

	public void setIsSSUploaded(Boolean isSSUploaded) {
		this.isSSUploaded = isSSUploaded;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUriPath() {
		return uriPath;
	}

	public void setUriPath(String uriPath) {
		this.uriPath = uriPath;
	}
}
