package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * TeamsDR Managed Site Detail Bean
 *
 * @author Syed Ali.
 * @createdAt 18/02/2021, Thursday, 14:35
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamsDRMSSiteDetailsBean {
	private Integer componentId;
	private String componentName;
	private String siteName;
	private String tataScopeOfWork;
	private String noOfUsersOnSite;
	private String siteTestingProvisioningSpoc;
	private String spocContactNumber;
	private String primaryTestUserDomainActivation;
	private String testUser1Credentials;
	private String secondaryTestUserDomainActivation;
	private String testUser2Credentials;

	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getTataScopeOfWork() {
		return tataScopeOfWork;
	}

	public void setTataScopeOfWork(String tataScopeOfWork) {
		this.tataScopeOfWork = tataScopeOfWork;
	}

	public String getNoOfUsersOnSite() {
		return noOfUsersOnSite;
	}

	public void setNoOfUsersOnSite(String noOfUsersOnSite) {
		this.noOfUsersOnSite = noOfUsersOnSite;
	}

	public String getSiteTestingProvisioningSpoc() {
		return siteTestingProvisioningSpoc;
	}

	public void setSiteTestingProvisioningSpoc(String siteTestingProvisioningSpoc) {
		this.siteTestingProvisioningSpoc = siteTestingProvisioningSpoc;
	}

	public String getSpocContactNumber() {
		return spocContactNumber;
	}

	public void setSpocContactNumber(String spocContactNumber) {
		this.spocContactNumber = spocContactNumber;
	}

	public String getPrimaryTestUserDomainActivation() {
		return primaryTestUserDomainActivation;
	}

	public void setPrimaryTestUserDomainActivation(String primaryTestUserDomainActivation) {
		this.primaryTestUserDomainActivation = primaryTestUserDomainActivation;
	}

	public String getTestUser1Credentials() {
		return testUser1Credentials;
	}

	public void setTestUser1Credentials(String testUser1Credentials) {
		this.testUser1Credentials = testUser1Credentials;
	}

	public String getSecondaryTestUserDomainActivation() {
		return secondaryTestUserDomainActivation;
	}

	public void setSecondaryTestUserDomainActivation(String secondaryTestUserDomainActivation) {
		this.secondaryTestUserDomainActivation = secondaryTestUserDomainActivation;
	}

	public String getTestUser2Credentials() {
		return testUser2Credentials;
	}

	public void setTestUser2Credentials(String testUser2Credentials) {
		this.testUser2Credentials = testUser2Credentials;
	}

}
