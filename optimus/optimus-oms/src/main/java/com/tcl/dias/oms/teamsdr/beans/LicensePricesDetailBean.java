package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * License prices detail
 * 
 * @author srraghav
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LicensePricesDetailBean {

	@JsonProperty("provider")
	private String provider;

	@JsonProperty("agreement_type")
	private String agreementType;

	@JsonProperty("list_of_license")
	private List<LicenseListPricesBean> listOfLicense;

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getAgreementType() {
		return agreementType;
	}

	public void setAgreementType(String agreementType) {
		this.agreementType = agreementType;
	}

	public List<LicenseListPricesBean> getListOfLicense() {
		return listOfLicense;
	}

	public void setListOfLicense(List<LicenseListPricesBean> listOfLicense) {
		this.listOfLicense = listOfLicense;
	}

	@Override
	public String toString() {
		return "LicensePricesDetailBean{" + "provider='" + provider + '\'' + ", agreementType='" + agreementType + '\''
				+ ", listOfLicense=" + listOfLicense + '}';
	}
}
