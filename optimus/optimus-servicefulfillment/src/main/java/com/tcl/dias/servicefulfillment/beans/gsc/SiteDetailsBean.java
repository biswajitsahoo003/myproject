package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.gsc.LocationBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SiteFunctionsBean;

public class SiteDetailsBean {

	private String corrolationId;
	private String siteName;
	private String siteType;
	private String ownerType;
	private String controlOfficeSiteAbbr;
	private String parentSiteAbbr;
	private String geoSpaceCode;
	private String withinColoSiteAbbr;
	private String addressSeqNo;
	private List<LocationBean> location;
	private List<SiteFunctionsBean> siteFunctions;
	private Boolean switchingUnitRequired;
	private String switchingUnitModel;
	private List<SiteContactBean> contact;

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

	public String getControlOfficeSiteAbbr() {
		return controlOfficeSiteAbbr;
	}

	public void setControlOfficeSiteAbbr(String controlOfficeSiteAbbr) {
		this.controlOfficeSiteAbbr = controlOfficeSiteAbbr;
	}

	public String getParentSiteAbbr() {
		return parentSiteAbbr;
	}

	public void setParentSiteAbbr(String parentSiteAbbr) {
		this.parentSiteAbbr = parentSiteAbbr;
	}

	public String getGeoSpaceCode() {
		return geoSpaceCode;
	}

	public void setGeoSpaceCode(String geoSpaceCode) {
		this.geoSpaceCode = geoSpaceCode;
	}

	public String getWithinColoSiteAbbr() {
		return withinColoSiteAbbr;
	}

	public void setWithinColoSiteAbbr(String withinColoSiteAbbr) {
		this.withinColoSiteAbbr = withinColoSiteAbbr;
	}

	public String getAddressSeqNo() {
		return addressSeqNo;
	}

	public void setAddressSeqNo(String addressSeqNo) {
		this.addressSeqNo = addressSeqNo;
	}

	public List<LocationBean> getLocation() {
		return location;
	}

	public void setLocation(List<LocationBean> location) {
		this.location = location;
	}

	public List<SiteFunctionsBean> getSiteFunctions() {
		return siteFunctions;
	}

	public void setSiteFunctions(List<SiteFunctionsBean> siteFunctions) {
		this.siteFunctions = siteFunctions;
	}

	public Boolean getSwitchingUnitRequired() {
		return switchingUnitRequired;
	}

	public void setSwitchingUnitRequired(Boolean switchingUnitRequired) {
		this.switchingUnitRequired = switchingUnitRequired;
	}

	public String getSwitchingUnitModel() {
		return switchingUnitModel;
	}

	public void setSwitchingUnitModel(String switchingUnitModel) {
		this.switchingUnitModel = switchingUnitModel;
	}

	public List<SiteContactBean> getContact() {
		return contact;
	}

	public void setContact(List<SiteContactBean> contact) {
		this.contact = contact;
	}

	public String getCorrolationId() {
		return corrolationId;
	}

	public void setCorrolationId(String corrolationId) {
		this.corrolationId = corrolationId;
	}
}
