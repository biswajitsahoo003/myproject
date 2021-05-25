package com.tcl.dias.serviceinventory.beans;

/**
 * Sdwan policy details request
 * 
 * @author Srinivasa Raghavan
 */
public class SdwanPolicyDetailsRequestBean {
	private String templateName;
	private String policyName;
	private String policyType;
	private String organisationName;
	private String directorRegion;
	private String accessPolicyName;

	
	public String getAccessPolicyName() {
		return accessPolicyName;
	}

	public void setAccessPolicyName(String accessPolicyName) {
		this.accessPolicyName = accessPolicyName;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public String getDirectorRegion() {
		return directorRegion;
	}

	public void setDirectorRegion(String directorRegion) {
		this.directorRegion = directorRegion;
	}
}
