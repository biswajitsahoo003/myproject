package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteBean {
	
	private String siteAbbr;
	private String siteName;
	private String siteType;
	private String ownerType;
	private String geoSpaceCode;
	private List<LocationBean> location;
	private List<AddressBean> address;
	private List<SiteFunctionsBean> siteFunctions;
	private List<SwitchingUnitsBean> switchingUnits;
	private String correlationID;
	
	
	public String getCorrelationID() {
		return correlationID;
	}

	public void setCorrelationID(String correlationID) {
		this.correlationID = correlationID;
	}

	public String getSiteAbbr() {
		return siteAbbr;
	}
	
	public void setSiteAbbr(String siteAbbr) {
		this.siteAbbr = siteAbbr;
	}
	
	public String getSiteName() {
		return siteName;
	}
	
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
	public String getSiteType() {
		return siteType;
	}
	
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	
	public String getOwnerType() {
		return ownerType;
	}
	
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	
	public String getGeoSpaceCode() {
		return geoSpaceCode;
	}
	
	public void setGeoSpaceCode(String geoSpaceCode) {
		this.geoSpaceCode = geoSpaceCode;
	}
	
	public List<LocationBean> getLocation() {
		return location;
	}
	
	public void setLocation(List<LocationBean> location) {
		this.location = location;
	}
	
	public List<AddressBean> getAddress() {
		return address;
	}
	
	public void setAddress(List<AddressBean> address) {
		this.address = address;
	}
	
	public List<SiteFunctionsBean> getSiteFunctions() {
		return siteFunctions;
	}
	
	public void setSiteFunctions(List<SiteFunctionsBean> siteFunctions) {
		this.siteFunctions = siteFunctions;
	}
	
	public List<SwitchingUnitsBean> getSwitchingUnits() {
		return switchingUnits;
	}
	
	public void setSwitchingUnits(List<SwitchingUnitsBean> switchingUnits) {
		this.switchingUnits = switchingUnits;
	}
	

}
