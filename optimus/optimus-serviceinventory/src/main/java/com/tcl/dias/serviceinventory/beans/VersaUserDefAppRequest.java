package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VersaUserDefAppRequest implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("user-defined-application")
	private VersaUserDefinedApplications userDefinedApplications;
	
	public VersaUserDefinedApplications getUserDefinedApplications() {
		return userDefinedApplications;
	}
	public void setUserDefinedApplications(VersaUserDefinedApplications userDefinedApplications) {
		this.userDefinedApplications = userDefinedApplications;
	}
	@Override
	public String toString() {
		return "user-defined-application [userDefinedApplications=" + userDefinedApplications + "]";
	}
	
	
	
	

}
