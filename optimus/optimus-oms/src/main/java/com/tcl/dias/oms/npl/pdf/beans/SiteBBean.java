package com.tcl.dias.oms.npl.pdf.beans;
/**
 * 
 * This file contains the SiteBBean.java class.
 * 
 *
 * @author NITHYA S
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
public class SiteBBean {

	private String siteAddress;
	private String city;
	private String billingAddress;
	private String gstnDetails;
	private String llProvider;
	
	public String getSiteAddress() {
		return siteAddress;
	}
	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}
	public String getGstnDetails() {
		return gstnDetails;
	}
	public void setGstnDetails(String gstnDetails) {
		this.gstnDetails = gstnDetails;
	}
	public String getLlProvider() {
		return llProvider;
	}
	public void setLlProvider(String llProvider) {
		this.llProvider = llProvider;
	}
	
	
}
