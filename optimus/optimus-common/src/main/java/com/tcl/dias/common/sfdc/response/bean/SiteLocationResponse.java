
package com.tcl.dias.common.sfdc.response.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * SiteLocationResponse.class is used for sfdc
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteLocationResponse {

	private String siteName;
	private String siteId;
	private String sfdcSiteName;
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
		return "SiteLocationResponse [siteName=" + siteName + ", siteId=" + siteId + ", sfdcSiteName=" + sfdcSiteName
				+ ", location=" + location + "]";
	}

}
