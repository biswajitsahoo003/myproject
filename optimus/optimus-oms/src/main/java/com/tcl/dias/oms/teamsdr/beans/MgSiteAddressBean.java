package com.tcl.dias.oms.teamsdr.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Mg site address
 * 
 * @author srraghav
 */
public class MgSiteAddressBean {

	@JsonProperty("city_id")
	private Integer cityId;

	@JsonProperty("site_address1")
	private String siteAddress1;

	@JsonProperty("site_address2")
	private String siteAddress2;

	@JsonProperty("site_address3")
	private String siteAddress3;

	@JsonProperty("city")
	private String city;

	@JsonProperty("state")
	private String state;

	@JsonProperty("pincode")
	private String pinCode;

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getSiteAddress1() {
		return siteAddress1;
	}

	public void setSiteAddress1(String siteAddress1) {
		this.siteAddress1 = siteAddress1;
	}

	public String getSiteAddress2() {
		return siteAddress2;
	}

	public void setSiteAddress2(String siteAddress2) {
		this.siteAddress2 = siteAddress2;
	}

	public String getSiteAddress3() {
		return siteAddress3;
	}

	public void setSiteAddress3(String siteAddress3) {
		this.siteAddress3 = siteAddress3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
}
