package com.tcl.dias.serviceinventory.beans;

import com.tcl.dias.common.beans.Attributes;

import java.util.List;

/**
 * Bean for Underlay sites
 * 
 * @author Srinivasa Raghavan
 */
public class CpeUnderlaySitesBean {

	private Integer id;
	private String siteName;
	private String siteStatus;
	private String cpeName;
	private Integer assetId;
	private List<String> controllers;
	private List<Attributes> links;
	private String underlayProductName;

	public CpeUnderlaySitesBean() {
		this.siteStatus = "Offline";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteStatus() {
		return siteStatus;
	}

	public void setSiteStatus(String siteStatus) {
		this.siteStatus = siteStatus;
	}

	public String getCpeName() {
		return cpeName;
	}

	public void setCpeName(String cpeName) {
		this.cpeName = cpeName;
	}

	public Integer getAssetId() {
		return assetId;
	}

	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}

	public List<String> getControllers() {
		return controllers;
	}

	public void setControllers(List<String> controllers) {
		this.controllers = controllers;
	}

	public List<Attributes> getLinks() {
		return links;
	}

	public void setLinks(List<Attributes> links) {
		this.links = links;
	}

	public String getUnderlayProductName() {
		return underlayProductName;
	}

	public void setUnderlayProductName(String underlayProductName) {
		this.underlayProductName = underlayProductName;
	}

	@Override
	public String toString() {
		return "CpeUnderlaySitesBean [id=" + id + ", siteName=" + siteName + ", siteStatus=" + siteStatus + ", cpeName="
				+ cpeName + ", assetId=" + assetId + ", controllers=" + controllers + ", links=" + links
				+ ", underlayProductName=" + underlayProductName + "]";
	}

	
}