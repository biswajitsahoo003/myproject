package com.tcl.dias.auth.beans;

public class UserConcernRequest {

	private String phoneNumber;
	private String customerName;
	private String issueType;
	private String browseUrl;
	private String description;
	private String browserName;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getBrowseUrl() {
		return browseUrl;
	}

	public void setBrowseUrl(String browseUrl) {
		this.browseUrl = browseUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBrowserName() {
		return browserName;
	}

	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}

	@Override
	public String toString() {
		return "UserConcernRequest [phoneNumber=" + phoneNumber + ", customerName=" + customerName + ", issueType="
				+ issueType + ", browseUrl=" + browseUrl + ", description=" + description + "]";
	}

}
