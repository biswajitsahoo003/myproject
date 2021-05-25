package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;

public class ApplicationInfoRequestBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String applicationName;
	private String organisationName;
	private String templateName;
	private String cpeName;
	private String applicationType;
	private String directoryRegion;
	private VersaUserDefAppRequest versaUserDefAppRequest;
	
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getOrganisationName() {
		return organisationName;
	}
	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getCpeName() {
		return cpeName;
	}
	public void setCpeName(String cpeName) {
		this.cpeName = cpeName;
	}
	public String getApplicationType() {
		return applicationType;
	}
	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}
	public String getDirectoryRegion() {
		return directoryRegion;
	}
	public void setDirectoryRegion(String directoryRegion) {
		this.directoryRegion = directoryRegion;
	}	
	public VersaUserDefAppRequest getVersaUserDefAppRequest() {
		return versaUserDefAppRequest;
	}
	public void setVersaUserDefAppRequest(VersaUserDefAppRequest versaUserDefAppRequest) {
		this.versaUserDefAppRequest = versaUserDefAppRequest;
	}
	@Override
	public String toString() {
		return "ApplicationInfoRequestBean [applicationName=" + applicationName + ", organisationName="
				+ organisationName + ", templateName=" + templateName + ", cpeName=" + cpeName + ", applicationType="
				+ applicationType + ", directoryRegion=" + directoryRegion + ", versaUserDefAppRequest="
				+ versaUserDefAppRequest + "]";
	}
	
	
	
	
	
}
