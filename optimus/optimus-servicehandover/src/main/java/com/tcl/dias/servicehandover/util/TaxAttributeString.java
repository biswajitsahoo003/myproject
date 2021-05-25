package com.tcl.dias.servicehandover.util;

public class TaxAttributeString {

	private String cpsName;
	private String taxExemptRef;
	private String taxExemptTxt;
	private String siteACity;
	private String siteAState;
	private String siteACountry;
	private String siteANrcChargable;
	private String siteAMrcChargable;
	private String siteATaxRate;
	private String siteBCity;
	private String siteBState;
	private String siteBCountry;
	private String siteBNrcChargable;
	private String siteBMrcChargable;
	private String siteBTaxRate;
	public String getCpsName() {
		return cpsName;
	}
	public void setCpsName(String cpsName) {
		this.cpsName = cpsName;
	}
	public String getTaxExemptRef() {
		return taxExemptRef;
	}
	public void setTaxExemptRef(String taxExemptRef) {
		this.taxExemptRef = taxExemptRef;
	}
	public String getTaxExemptTxt() {
		return taxExemptTxt;
	}
	public void setTaxExemptTxt(String taxExemptTxt) {
		this.taxExemptTxt = taxExemptTxt;
	}
	public String getSiteACity() {
		return siteACity;
	}
	public void setSiteACity(String siteACity) {
		this.siteACity = siteACity;
	}
	public String getSiteAState() {
		return siteAState;
	}
	public void setSiteAState(String siteAState) {
		this.siteAState = siteAState;
	}
	public String getSiteACountry() {
		return siteACountry;
	}
	public void setSiteACountry(String siteACountry) {
		this.siteACountry = siteACountry;
	}
	public String getSiteANrcChargable() {
		return siteANrcChargable;
	}
	public void setSiteANrcChargable(String siteANrcChargable) {
		this.siteANrcChargable = siteANrcChargable;
	}
	public String getSiteAMrcChargable() {
		return siteAMrcChargable;
	}
	public void setSiteAMrcChargable(String siteAMrcChargable) {
		this.siteAMrcChargable = siteAMrcChargable;
	}
	public String getSiteATaxRate() {
		return siteATaxRate;
	}
	public void setSiteATaxRate(String siteATaxRate) {
		this.siteATaxRate = siteATaxRate;
	}
	public String getSiteBCity() {
		return siteBCity;
	}
	public void setSiteBCity(String siteBCity) {
		this.siteBCity = siteBCity;
	}
	public String getSiteBState() {
		return siteBState;
	}
	public void setSiteBState(String siteBState) {
		this.siteBState = siteBState;
	}
	public String getSiteBCountry() {
		return siteBCountry;
	}
	public void setSiteBCountry(String siteBCountry) {
		this.siteBCountry = siteBCountry;
	}
	public String getSiteBNrcChargable() {
		return siteBNrcChargable;
	}
	public void setSiteBNrcChargable(String siteBNrcChargable) {
		this.siteBNrcChargable = siteBNrcChargable;
	}
	public String getSiteBMrcChargable() {
		return siteBMrcChargable;
	}
	public void setSiteBMrcChargable(String siteBMrcChargable) {
		this.siteBMrcChargable = siteBMrcChargable;
	}
	public String getSiteBTaxRate() {
		return siteBTaxRate;
	}
	public void setSiteBTaxRate(String siteBTaxRate) {
		this.siteBTaxRate = siteBTaxRate;
	}

	@Override
	public String toString() {
		return "CPS_NAME=" + cpsName + ";TAX_EXEMPT_REF=" + taxExemptRef + ";TAX_EXEMPT_TXT=" + taxExemptTxt
				+ ";SITEA_CITY=" + siteACity + ";SITEA_STATE=" + siteAState + ";SITEA_COUNTRY=" + siteACountry
				+ ";SITEA_NRC_CHARGEABLE_PRCNT=" + siteANrcChargable + ";SITEA_MRC_CHARGEABLE_PRCNT="
				+ siteAMrcChargable + ";SITEA_TAX_RATE=" + siteATaxRate + ";SITEB_CITY=" + siteBCity + ";SITEB_STATE="
				+ siteBState + ";SITEB_COUNTRY=" + siteBCountry + ";SITEB_NRC_CHARGEABLE_PRCNT=" + siteBNrcChargable
				+ ";SITEB_MRC_CHARGEABLE_PRCNT=" + siteBMrcChargable + ";SITEB_TAX_RATE=" + siteBTaxRate + ";";
	}

}
