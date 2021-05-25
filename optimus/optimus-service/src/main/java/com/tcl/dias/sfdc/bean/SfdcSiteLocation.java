
package com.tcl.dias.sfdc.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * This file contains the CreateOpportunityBean.java class.
 * used to connect with sdfc
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "SiteLocationID", "Location", "City", "State", "Country", "SiteMRC", "SiteNRC","Link_Type__c","CircuitServiceID" })
public class SfdcSiteLocation  extends BaseBean{

	@JsonProperty("SiteLocationID")
	private String siteLocationID;
	@JsonProperty("Location")
	private String location;
	@JsonProperty("City")
	private String city;
	@JsonProperty("State")
	private String state;
	@JsonProperty("Country")
	private String country;
	@JsonProperty("SiteMRC")
	private String siteMRC;
	@JsonProperty("SiteNRC")
	private String siteNRC;
	
	@JsonProperty("Link_Type__c")
	private String linkType;

	@JsonProperty("CircuitServiceID")
	private String currentCircuitServiceId;

	@JsonProperty("SiteLocationID")
	public String getSiteLocationID() {
		return siteLocationID;
	}

	@JsonProperty("SiteLocationID")
	public void setSiteLocationID(String siteLocationID) {
		this.siteLocationID = siteLocationID;
	}

	@JsonProperty("Location")
	public String getLocation() {
		return location;
	}

	@JsonProperty("Location")
	public void setLocation(String location) {
		this.location = location;
	}

	@JsonProperty("City")
	public String getCity() {
		return city;
	}

	@JsonProperty("City")
	public void setCity(String city) {
		this.city = city;
	}

	@JsonProperty("State")
	public String getState() {
		return state;
	}

	@JsonProperty("State")
	public void setState(String state) {
		this.state = state;
	}

	@JsonProperty("Country")
	public String getCountry() {
		return country;
	}

	@JsonProperty("Country")
	public void setCountry(String country) {
		this.country = country;
	}

	@JsonProperty("SiteMRC")
	public String getSiteMRC() {
		return siteMRC;
	}

	@JsonProperty("SiteMRC")
	public void setSiteMRC(String siteMRC) {
		this.siteMRC = siteMRC;
	}

	@JsonProperty("SiteNRC")
	public String getSiteNRC() {
		return siteNRC;
	}

	@JsonProperty("SiteNRC")
	public void setSiteNRC(String siteNRC) {
		this.siteNRC = siteNRC;
	}

	@JsonProperty("Link_Type__c")
	public String getLinkType() {
		return linkType;
	}

	@JsonProperty("Link_Type__c")
	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	@JsonProperty("CircuitServiceID")
	public String getCurrentCircuitServiceId() {
		return currentCircuitServiceId;
	}

	@JsonProperty("CircuitServiceID")
	public void setCurrentCircuitServiceId(String currentCircuitServiceId) {
		this.currentCircuitServiceId = currentCircuitServiceId;
	}

}
