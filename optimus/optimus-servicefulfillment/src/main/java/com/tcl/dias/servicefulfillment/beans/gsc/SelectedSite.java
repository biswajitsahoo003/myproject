package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.gsc.SiteFunctionsBean;

public class SelectedSite {
	
	private String siteId;
	private String siteName;
	private List<String> siteFunctions;
	private String equipmentId;
	private String AddressId;
	private String address;
	private String cityAbbr;
	private String cityName;
	private String stateOrProvinceAbbr;
	private String stateOrProvinceName;
	private String countryAbbr;
	private String countryName;
	private String postalCd;
	private String geoCode;
	
	public String getSiteId() {
		return siteId;
	}
	
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	
	public String getSiteName() {
		return siteName;
	}
	
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
	
	public List<String> getSiteFunctions() {
		return siteFunctions;
	}

	public void setSiteFunctions(List<String> siteFunctions) {
		this.siteFunctions = siteFunctions;
	}

	public String getEquipmentId() {
		return equipmentId;
	}
	
	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
	
	public String getAddressId() {
		return AddressId;
	}
	
	public void setAddressId(String addressId) {
		AddressId = addressId;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getCityAbbr() {
		return cityAbbr;
	}

	public void setCityAbbr(String cityAbbr) {
		this.cityAbbr = cityAbbr;
	}

	public String getCityName() {
		return cityName;
	}
	
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public String getStateOrProvinceAbbr() {
		return stateOrProvinceAbbr;
	}
	
	public void setStateOrProvinceAbbr(String stateOrProvinceAbbr) {
		this.stateOrProvinceAbbr = stateOrProvinceAbbr;
	}
	
	public String getStateOrProvinceName() {
		return stateOrProvinceName;
	}
	
	public void setStateOrProvinceName(String stateOrProvinceName) {
		this.stateOrProvinceName = stateOrProvinceName;
	}
	
	public String getCountryAbbr() {
		return countryAbbr;
	}
	
	public void setCountryAbbr(String countryAbbr) {
		this.countryAbbr = countryAbbr;
	}
	
	public String getCountryName() {
		return countryName;
	}
	
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	public String getPostalCd() {
		return postalCd;
	}
	
	public void setPostalCd(String postalCd) {
		this.postalCd = postalCd;
	}
	
	public String getGeoCode() {
		return geoCode;
	}
	
	public void setGeoCode(String geoCode) {
		this.geoCode = geoCode;
	}

}
