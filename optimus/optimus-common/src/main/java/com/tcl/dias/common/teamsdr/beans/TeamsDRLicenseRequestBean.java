package com.tcl.dias.common.teamsdr.beans;

import java.util.Set;

/**
 * This file contains TeamsDRLicenseRequestBean.class
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class TeamsDRLicenseRequestBean {
	private Set<String> countries;
	private String agreementType;

	public TeamsDRLicenseRequestBean() {

	}

	public Set<String> getCountries() {
		return countries;
	}

	public void setCountries(Set<String> countries) {
		this.countries = countries;
	}

	public String getAgreementType() {
		return agreementType;
	}

	public void setAgreementType(String agreementType) {
		this.agreementType = agreementType;
	}

	@Override
	public String toString() {
		return "TeamsDRLicenseRequestBean{" + "countries=" + countries + ", agreementType='" + agreementType + '\''
				+ '}';
	}
}
