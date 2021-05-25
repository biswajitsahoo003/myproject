package com.tcl.dias.servicefulfillment.beans.gsc;

public class SiteContactBean {
	
	private String phoneNumber;
	private String extnNo;
	private String emailAddress;
	private String missingReason;
	private String remark;
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getExtnNo() {
		return extnNo;
	}
	
	public void setExtnNo(String extnNo) {
		this.extnNo = extnNo;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getMissingReason() {
		return missingReason;
	}
	
	public void setMissingReason(String missingReason) {
		this.missingReason = missingReason;
	}
	

}
