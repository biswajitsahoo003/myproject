package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

/**
 * This file contains the TeamsDRLicenseConfigurationBean.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class TeamsDRLicenseComponentsBean {
	private String agreementType;
	private List<TeamsDRLicenseConfigurationBean> licenseConfigurations;

	public TeamsDRLicenseComponentsBean() {
	}

	public String getAgreementType() {
		return agreementType;
	}

	public void setAgreementType(String agreementType) {
		this.agreementType = agreementType;
	}

	public List<TeamsDRLicenseConfigurationBean> getLicenseConfigurations() {
		return licenseConfigurations;
	}

	public void setLicenseConfigurations(List<TeamsDRLicenseConfigurationBean> licenseConfigurations) {
		this.licenseConfigurations = licenseConfigurations;
	}

	@Override
	public String toString() {
		return "TeamsDRLicenseComponentsBean{" + "agreementType='" + agreementType + '\'' + ", licenseConfigurations="
				+ licenseConfigurations + '}';
	}
}
