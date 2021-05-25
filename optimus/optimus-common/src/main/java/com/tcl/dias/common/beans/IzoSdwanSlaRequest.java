package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IzoSdwanSlaRequest {
	
	private Integer serviceId;
	
	private String productName;
	
	private String cityName;
	
	private String countryName;
	
	private String siteTypeName;
	
	private String sltVarient;
	
	private String accessTopology;
	
	private String vendorName;

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getSiteTypeName() {
		return siteTypeName;
	}

	public void setSiteTypeName(String siteTypeName) {
		this.siteTypeName = siteTypeName;
	}

	public String getSltVarient() {
		return sltVarient;
	}

	public void setSltVarient(String sltVarient) {
		this.sltVarient = sltVarient;
	}

	public String getAccessTopology() {
		return accessTopology;
	}

	public void setAccessTopology(String accessTopology) {
		this.accessTopology = accessTopology;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	
	
	

}
