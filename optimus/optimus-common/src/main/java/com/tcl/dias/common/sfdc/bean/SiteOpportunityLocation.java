package com.tcl.dias.common.sfdc.bean;

/**
 * This file contains the SiteOpportunityLocation.java class.
 *used for sfdc
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SiteOpportunityLocation {

	private String siteLocationID;
	private String location;
	private String city;
	private String state;
	private String country;
	private Double siteMRC;
	private Double siteNRC;
	
	// introduced for NPL
	private Double siteARC;
	private String linkCode;

		//introduced for MACD
	private String currentCircuitServiceId;

	/**
	 * @return the siteLocationID
	 */
	public String getSiteLocationID() {
		return siteLocationID;
	}
	/**
	 * @param siteLocationID the siteLocationID to set
	 */
	public void setSiteLocationID(String siteLocationID) {
		this.siteLocationID = siteLocationID;
	}
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the siteMRC
	 */
	public Double getSiteMRC() {
		return siteMRC;
	}
	/**
	 * @param siteMRC the siteMRC to set
	 */
	public void setSiteMRC(Double siteMRC) {
		this.siteMRC = siteMRC;
	}
	/**
	 * @return the siteNRC
	 */
	public Double getSiteNRC() {
		return siteNRC;
	}
	/**
	 * @param siteNRC the siteNRC to set
	 */
	public void setSiteNRC(Double siteNRC) {
		this.siteNRC = siteNRC;
	}
	public Double getSiteARC() {
		return siteARC;
	}
	public void setSiteARC(Double siteARC) {
		this.siteARC = siteARC;
	}
	public String getLinkCode() {
		return linkCode;
	}
	public void setLinkCode(String linkCode) {
		this.linkCode = linkCode;
	}

	public String getCurrentCircuitServiceId() {
		return currentCircuitServiceId;
	}

	public void setCurrentCircuitServiceId(String currentCircuitServiceId) {
		this.currentCircuitServiceId = currentCircuitServiceId;
	}





}
