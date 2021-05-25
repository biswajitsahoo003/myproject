package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file Contains MDM Address  information
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "countryName",
    "provinceStateValue",
    "zipPostalCode",
    "standardizedAddressExternally",
    "addressUsageValue",
    "createdDate",
    "addressLineOne",
    "city"  
    
})

public class MDMAddress {
	 @JsonProperty("countryName")
	 private String countryName;
	 @JsonProperty("provinceStateValue")
	 private String provinceStateValue;
	 @JsonProperty("zipPostalCode")
	 private String zipPostalCode;
	 @JsonProperty("standardizedAddressExternally")
	 private String standardizedAddressExternally;
	 @JsonProperty("addressUsageValue")
	 private String addressUsageValue;
	 @JsonProperty("createdDate")
	 private String createdDate;
	 @JsonProperty("addressLineOne")
	 private String addressLineOne;
	 @JsonProperty("city")
	 private String city;
	 
	@JsonProperty("countryName") 
	public String getCountryName() {
		return countryName;
	}
	@JsonProperty("countryName")
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	@JsonProperty("provinceStateValue")
	public String getProvinceStateValue() {
		return provinceStateValue;
	}
	@JsonProperty("provinceStateValue")
	public void setProvinceStateValue(String provinceStateValue) {
		this.provinceStateValue = provinceStateValue;
	}
	
	@JsonProperty("zipPostalCode")
	public String getZipPostalCode() {
		return zipPostalCode;
	}
	@JsonProperty("zipPostalCode")
	public void setZipPostalCode(String zipPostalCode) {
		this.zipPostalCode = zipPostalCode;
	}
	
	@JsonProperty("standardizedAddressExternally")
	public String getStandardizedAddressExternally() {
		return standardizedAddressExternally;
	}
	@JsonProperty("standardizedAddressExternally")
	public void setStandardizedAddressExternally(String standardizedAddressExternally) {
		this.standardizedAddressExternally = standardizedAddressExternally;
	}
	
	@JsonProperty("addressUsageValue")
	public String getAddressUsageValue() {
		return addressUsageValue;
	}
	@JsonProperty("addressUsageValue")
	public void setAddressUsageValue(String addressUsageValue) {
		this.addressUsageValue = addressUsageValue;
	}
	
	@JsonProperty("createdDate")
	public String getCreatedDate() {
		return createdDate;
	}
	@JsonProperty("createdDate")
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	@JsonProperty("addressLineOne")
	public String getAddressLineOne() {
		return addressLineOne;
	}
	@JsonProperty("addressLineOne")
	public void setAddressLineOne(String addressLineOne) {
		this.addressLineOne = addressLineOne;
	}
	
	@JsonProperty("city")
	public String getCity() {
		return city;
	}
	@JsonProperty("city")
	public void setCity(String city) {
		this.city = city;
	}
	 
	 

}
