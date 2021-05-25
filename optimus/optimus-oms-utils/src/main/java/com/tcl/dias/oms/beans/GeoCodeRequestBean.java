package com.tcl.dias.oms.beans;


	import com.fasterxml.jackson.annotation.JsonInclude;
	import com.fasterxml.jackson.annotation.JsonProperty;
	import com.fasterxml.jackson.annotation.JsonPropertyOrder;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonPropertyOrder({
	"ClientNumber",
	"ValidationKey",
	"PrimaryAddressLine",
	"City",
	"State",
	"ZIPCode"
	})
	public class GeoCodeRequestBean {

	@JsonProperty("ClientNumber")
	private String clientNumber;
	@JsonProperty("ValidationKey")
	private String validationKey;
	@JsonProperty("PrimaryAddressLine")
	private String primaryAddressLine;
	@JsonProperty("City")
	private String city;
	@JsonProperty("State")
	private String state;
	@JsonProperty("ZIPCode")
	private String zIPCode;
	
	@JsonProperty("ClientNumber")
	public String getClientNumber() {
	return clientNumber;
	}

	@JsonProperty("ClientNumber")
	public void setClientNumber(String clientNumber) {
	this.clientNumber = clientNumber;
	}

	@JsonProperty("ValidationKey")
	public String getValidationKey() {
	return validationKey;
	}

	@JsonProperty("ValidationKey")
	public void setValidationKey(String validationKey) {
	this.validationKey = validationKey;
	}

	@JsonProperty("PrimaryAddressLine")
	public String getPrimaryAddressLine() {
	return primaryAddressLine;
	}

	@JsonProperty("PrimaryAddressLine")
	public void setPrimaryAddressLine(String primaryAddressLine) {
	this.primaryAddressLine = primaryAddressLine;
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

	@JsonProperty("ZIPCode")
	public String getZIPCode() {
	return zIPCode;
	}

	@JsonProperty("ZIPCode")
	public void setZIPCode(String zIPCode) {
	this.zIPCode = zIPCode;
	}

	
}
