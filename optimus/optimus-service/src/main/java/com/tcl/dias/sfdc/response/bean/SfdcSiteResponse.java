package com.tcl.dias.sfdc.response.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SfdcSiteResponse.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "SiteName", "SiteID", "SFDCSiteName", "Location", })
public class SfdcSiteResponse {

	@JsonProperty("SiteName")
	private String siteName;
	@JsonProperty("SiteID")
	private String siteId;
	@JsonProperty("SFDCSiteName")
	private String sfdcSiteName;
	@JsonProperty("Location")
	private String location;

	/**
	 * @return the siteName
	 */
	public String getSiteName() {
		return siteName;
	}

	/**
	 * @param siteName
	 *            the siteName to set
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	/**
	 * @return the siteId
	 */
	public String getSiteId() {
		return siteId;
	}

	/**
	 * @param siteId
	 *            the siteId to set
	 */
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	/**
	 * @return the sfdcSiteName
	 */
	public String getSfdcSiteName() {
		return sfdcSiteName;
	}

	/**
	 * @param sfdcSiteName
	 *            the sfdcSiteName to set
	 */
	public void setSfdcSiteName(String sfdcSiteName) {
		this.sfdcSiteName = sfdcSiteName;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * toString
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return "SfdcSiteResponse [siteName=" + siteName + ", siteId=" + siteId + ", sfdcSiteName=" + sfdcSiteName
				+ ", location=" + location + "]";
	}

}
