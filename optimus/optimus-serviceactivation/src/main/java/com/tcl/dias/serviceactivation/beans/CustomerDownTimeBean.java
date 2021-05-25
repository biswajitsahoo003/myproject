package com.tcl.dias.serviceactivation.beans;

import java.io.Serializable;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class CustomerDownTimeBean extends BaseRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public String downtimeDuration;
	public String fromTime;
	public String toTime;
	public String contactPerson;
	public String contactMobileNumber;
	public String contactMailId;
	
	public String getDowntimeDuration() {
		return downtimeDuration;
	}
	public void setDowntimeDuration(String downtimeDuration) {
		this.downtimeDuration = downtimeDuration;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getContactMobileNumber() {
		return contactMobileNumber;
	}
	public void setContactMobileNumber(String contactMobileNumber) {
		this.contactMobileNumber = contactMobileNumber;
	}
	public String getContactMailId() {
		return contactMailId;
	}
	public void setContactMailId(String contactMailId) {
		this.contactMailId = contactMailId;
	}
	
	
	
}
