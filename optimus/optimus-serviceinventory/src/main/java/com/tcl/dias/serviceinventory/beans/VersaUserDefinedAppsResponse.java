package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * VersaUserDefinedAppsResponse bean class
 * @author archchan
 *
 */
public class VersaUserDefinedAppsResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("user-defined-application")
	private List<VersaUserDefinedApplications> userDefinedApplications;
	
	public List<VersaUserDefinedApplications> getUserDefinedApplications() {
		return userDefinedApplications;
	}
	public void setUserDefinedApplications(List<VersaUserDefinedApplications> userDefinedApplications) {
		this.userDefinedApplications = userDefinedApplications;
	}
	@Override
	public String toString() {
		return "VersaUserDefinedAppsResponse [userDefinedApplications=" + userDefinedApplications + "]";
	}
	
	

}
