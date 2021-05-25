package com.tcl.dias.oms.teamsdr.beans;

import com.tcl.dias.oms.beans.QuoteProductComponentBean;

import java.util.List;

/**
 * This file contains the TeamsDRLicenseConfigurationBean.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class TeamsDRLicenseConfigurationBean {
	private String provider;
	private List<QuoteProductComponentBean> licenseComponents;
	private List<TeamsDRLicenseBean> licenseDetails;

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public List<QuoteProductComponentBean> getLicenseComponents() {
		return licenseComponents;
	}

	public void setLicenseComponents(List<QuoteProductComponentBean> licenseComponents) {
		this.licenseComponents = licenseComponents;
	}

	public List<TeamsDRLicenseBean> getLicenseDetails() {
		return licenseDetails;
	}

	public void setLicenseDetails(List<TeamsDRLicenseBean> licenseDetails) {
		this.licenseDetails = licenseDetails;
	}

	@Override
	public String toString() {
		return "TeamsDRLicenseConfigurationBean{" +
				"provider='" + provider + '\'' +
				", licenseComponents=" + licenseComponents +
				", licenseDetails=" + licenseDetails +
				'}';
	}
}
