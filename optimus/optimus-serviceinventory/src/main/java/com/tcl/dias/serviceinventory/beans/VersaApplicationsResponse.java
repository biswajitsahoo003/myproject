package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * VersaApplicationsResponse class
 * @author archchan
 *
 */
public class VersaApplicationsResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("applications")
	private List<VersaPredefinedApplications> applications;

	public List<VersaPredefinedApplications> getApplications() {
		return applications;
	}

	public void setApplications(List<VersaPredefinedApplications> applications) {
		this.applications = applications;
	}
	
	
	

}
