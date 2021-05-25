package com.tcl.dias.oms.webex.beans;

/**
 * This file was created for populating Cisco Deal Attributes
 *
 */
public class CiscoDealAttributesBean {

	private String customerName;
	private String licenseType;
	private String customerAddress;

	public CiscoDealAttributesBean() {
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	@Override
	public String toString() {
		return "CiscoDealAttributesBean{" + ", customerName='" + customerName + '\'' + ", licenseType='" + licenseType
				+ '\'' + ", customerAddress='" + customerAddress + '\'' + '}';
	}
}
