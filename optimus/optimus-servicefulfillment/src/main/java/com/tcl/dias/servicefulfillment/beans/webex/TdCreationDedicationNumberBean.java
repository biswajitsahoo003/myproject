package com.tcl.dias.servicefulfillment.beans.webex;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class TdCreationDedicationNumberBean extends TaskDetailsBaseBean {
	
	private String telephonyDomain;
	private String routingProfile;
	private String cluster;
	private String phoneNumbersA;
	private String creationDScompletionDate;
	
	public String getTelephonyDomain() {
		return telephonyDomain;
	}
	public void setTelephonyDomain(String telephonyDomain) {
		this.telephonyDomain = telephonyDomain;
	}
	public String getRoutingProfile() {
		return routingProfile;
	}
	public void setRoutingProfile(String routingProfile) {
		this.routingProfile = routingProfile;
	}
	public String getCluster() {
		return cluster;
	}
	public void setCluster(String cluster) {
		this.cluster = cluster;
	}
	public String getPhoneNumbersA() {
		return phoneNumbersA;
	}
	public void setPhoneNumbersA(String phoneNumbersA) {
		this.phoneNumbersA = phoneNumbersA;
	}
	public String getCreationDScompletionDate() {
		return creationDScompletionDate;
	}
	public void setCreationDScompletionDate(String creationDScompletionDate) {
		this.creationDScompletionDate = creationDScompletionDate;
	}
	
	

}
