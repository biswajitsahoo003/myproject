package com.tcl.dias.servicehandover.util;

public class CatalystPayPerUseDetailRequest {

	private String sfdcId;
	
	private String commisionedDate;
	
	private String customerName;
	
	private String dcLocationCode;

	public String getSfdcId() {
		return sfdcId;
	}

	public void setSfdcId(String sfdcId) {
		this.sfdcId = sfdcId;
	}

	public String getCommisionedDate() {
		return commisionedDate;
	}

	public void setCommisionedDate(String commisionedDate) {
		this.commisionedDate = commisionedDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDcLocationCode() {
		return dcLocationCode;
	}

	public void setDcLocationCode(String dcLocationCode) {
		this.dcLocationCode = dcLocationCode;
	}
	
}