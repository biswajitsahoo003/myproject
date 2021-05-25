package com.tcl.dias.servicefulfillment.beans.teamsdr;

import java.util.List;

/**
 * Management and monitoring site bean
 */
public class ManagementAndMonitoringCountryDetailBean {

	// For management and monitoring
	private String country;
	private List<ManagementAndMonitoringSiteDetailBean> managementAndMonitoringSites;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<ManagementAndMonitoringSiteDetailBean> getManagementAndMonitoringSites() {
		return managementAndMonitoringSites;
	}

	public void setManagementAndMonitoringSites(
			List<ManagementAndMonitoringSiteDetailBean> managementAndMonitoringSites) {
		this.managementAndMonitoringSites = managementAndMonitoringSites;
	}
}
