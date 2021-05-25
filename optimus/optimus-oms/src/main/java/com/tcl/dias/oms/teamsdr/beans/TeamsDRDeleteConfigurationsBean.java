package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

/**
 * This file contains the DeleteConfigurationsBean.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class TeamsDRDeleteConfigurationsBean {
	private List<Integer> mediagatewayConfig;
	private List<Integer> licenseConfig;
	private Boolean deleteAllLicenseAttr;

	public TeamsDRDeleteConfigurationsBean() {
	}

	public List<Integer> getMediagatewayConfig() {
		return mediagatewayConfig;
	}

	public void setMediagatewayConfig(List<Integer> mediagatewayConfig) {
		this.mediagatewayConfig = mediagatewayConfig;
	}

	public List<Integer> getLicenseConfig() {
		return licenseConfig;
	}

	public void setLicenseConfig(List<Integer> licenseConfig) {
		this.licenseConfig = licenseConfig;
	}

	public Boolean getDeleteAllLicenseAttr() {
		return deleteAllLicenseAttr;
	}

	public void setDeleteAllLicenseAttr(Boolean deleteAllLicenseAttr) {
		this.deleteAllLicenseAttr = deleteAllLicenseAttr;
	}

	@Override
	public String toString() {
		return "TeamsDRDeleteConfigurationsBean{" +
				"mediagatewayConfig=" + mediagatewayConfig +
				", licenseConfig=" + licenseConfig +
				", deleteAllLicenseAttr=" + deleteAllLicenseAttr +
				'}';
	}
}
