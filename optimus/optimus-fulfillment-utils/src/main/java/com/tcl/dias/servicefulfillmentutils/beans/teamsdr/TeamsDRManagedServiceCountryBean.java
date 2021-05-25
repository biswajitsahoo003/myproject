package com.tcl.dias.servicefulfillmentutils.beans.teamsdr;

import java.util.List;
import java.util.Map;

/**
 * Bean to store managed services country and site details
 */
public class TeamsDRManagedServiceCountryBean {
	private Integer countryCompId;
	private String countryName;
	private Map<String, Object> countryAttributes;
	private List<TeamsDRManagedServiceSiteDetails> managedServiceSiteDetails;

	public TeamsDRManagedServiceCountryBean() {
	}

	public Integer getCountryCompId() {
		return countryCompId;
	}

	public void setCountryCompId(Integer countryCompId) {
		this.countryCompId = countryCompId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public List<TeamsDRManagedServiceSiteDetails> getManagedServiceSiteDetails() {
		return managedServiceSiteDetails;
	}

	public void setManagedServiceSiteDetails(List<TeamsDRManagedServiceSiteDetails> managedServiceSiteDetails) {
		this.managedServiceSiteDetails = managedServiceSiteDetails;
	}

	public Map<String, Object> getCountryAttributes() {
		return countryAttributes;
	}

	public void setCountryAttributes(Map<String, Object> countryAttributes) {
		this.countryAttributes = countryAttributes;
	}
}
