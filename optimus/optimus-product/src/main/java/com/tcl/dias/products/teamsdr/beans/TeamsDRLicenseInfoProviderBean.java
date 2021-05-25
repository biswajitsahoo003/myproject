package com.tcl.dias.products.teamsdr.beans;

import java.util.List;

/**
 * Bean for sending static license info along with provider name
 * 
 * @author Srinivasa Raghavan
 */
public class TeamsDRLicenseInfoProviderBean {

	private String provider;
	private List<TeamsDRLicenseInfoBean> teamsDRLicenseInfos;

	public TeamsDRLicenseInfoProviderBean() {
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public List<TeamsDRLicenseInfoBean> getTeamsDRLicenseInfos() {
		return teamsDRLicenseInfos;
	}

	public void setTeamsDRLicenseInfos(List<TeamsDRLicenseInfoBean> teamsDRLicenseInfos) {
		this.teamsDRLicenseInfos = teamsDRLicenseInfos;
	}

	@Override
	public String toString() {
		return "TeamsDRLicenseInfoProviderBean{" + "provider='" + provider + '\'' + ", teamsDRLicenseInfos="
				+ teamsDRLicenseInfos + '}';
	}
}
