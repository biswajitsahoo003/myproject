package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

/**
 * Teams DR Order license component bean
 * 
 * @author Srinivasa Raghavan
 */
public class TeamsDROrderLicenseComponentsBean {
	private String agreementType;
	private List<TeamsDROrderLicenseConfigurationBean> licenseConfigurations;

	public TeamsDROrderLicenseComponentsBean() {
	}

	public String getAgreementType() {
		return agreementType;
	}

	public void setAgreementType(String agreementType) {
		this.agreementType = agreementType;
	}

	public List<TeamsDROrderLicenseConfigurationBean> getLicenseConfigurations() {
		return licenseConfigurations;
	}

	public void setLicenseConfigurations(List<TeamsDROrderLicenseConfigurationBean> licenseConfigurations) {
		this.licenseConfigurations = licenseConfigurations;
	}

	@Override
	public String toString() {
		return "TeamsDRLicenseComponentsBean{" + "agreementType='" + agreementType + '\'' + ", licenseConfigurations="
				+ licenseConfigurations + '}';
	}
}
