package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

import com.tcl.dias.oms.beans.OrderProductComponentBean;

/**
 * Teams DR Order License configuration bean
 * 
 * @author Srinivasa Raghavan
 */
public class TeamsDROrderLicenseConfigurationBean {
	private String provider;
	private List<OrderProductComponentBean> licenseComponents;
	private List<TeamsDROrderLicenseBean> licenseDetails;

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public List<OrderProductComponentBean> getLicenseComponents() {
		return licenseComponents;
	}

	public void setLicenseComponents(List<OrderProductComponentBean> licenseComponents) {
		this.licenseComponents = licenseComponents;
	}

	public List<TeamsDROrderLicenseBean> getLicenseDetails() {
		return licenseDetails;
	}

	public void setLicenseDetails(List<TeamsDROrderLicenseBean> licenseDetails) {
		this.licenseDetails = licenseDetails;
	}

	@Override
	public String toString() {
		return "TeamsDRLicenseConfigurationBean{" + "provider='" + provider + '\'' + ", licenseComponents="
				+ licenseComponents + ", licenseDetails=" + licenseDetails + '}';
	}
}
