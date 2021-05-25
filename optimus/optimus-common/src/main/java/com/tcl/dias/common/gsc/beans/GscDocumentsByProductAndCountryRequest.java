package com.tcl.dias.common.gsc.beans;

public class GscDocumentsByProductAndCountryRequest {
	private String productName;
	private String iso3CountryCode;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getIso3CountryCode() {
		return iso3CountryCode;
	}

	public void setIso3CountryCode(String iso3CountryCode) {
		this.iso3CountryCode = iso3CountryCode;
	}
}
