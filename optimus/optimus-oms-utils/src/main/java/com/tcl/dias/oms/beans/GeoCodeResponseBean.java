package com.tcl.dias.oms.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "BusinessUnit", "City", "ClientNumber", "ClientTracking", "County", "DayLightSaving", "FirmName",
		"GeoCode", "IsMatchLimitExceed", "Latitude", "Longitude", "Message", "PrimaryAddressLine", "ResultList",
		"ReturnCode", "Score", "SecondaryAddressLine", "State", "TimeZone", "TopMatchUnique", "ZIPCode", "ZIPPlus4" })
public class GeoCodeResponseBean {

	@JsonProperty("BusinessUnit")
	private String businessUnit;
	@JsonProperty("City")
	private String city;
	@JsonProperty("ClientNumber")
	private String clientNumber;
	@JsonProperty("ClientTracking")
	private String clientTracking;
	@JsonProperty("County")
	private String county;
	@JsonProperty("DayLightSaving")
	private Boolean dayLightSaving;
	@JsonProperty("FirmName")
	private String firmName;
	@JsonProperty("GeoCode")
	private String geoCode;
	@JsonProperty("IsMatchLimitExceed")
	private Boolean isMatchLimitExceed;
	@JsonProperty("Latitude")
	private Double latitude;
	@JsonProperty("Longitude")
	private Double longitude;
	@JsonProperty("Message")
	private String message;
	@JsonProperty("PrimaryAddressLine")
	private String primaryAddressLine;
	@JsonProperty("ResultList")
	private List<String> resultList = null;
	@JsonProperty("ReturnCode")
	private Integer returnCode;
	@JsonProperty("Score")
	private Double score;
	@JsonProperty("SecondaryAddressLine")
	private String secondaryAddressLine;
	@JsonProperty("State")
	private String state;
	@JsonProperty("TimeZone")
	private Integer timeZone;
	@JsonProperty("TopMatchUnique")
	private Boolean topMatchUnique;
	@JsonProperty("ZIPCode")
	private String zIPCode;
	@JsonProperty("ZIPPlus4")
	private String zIPPlus4;
	
	@JsonProperty("BusinessUnit")
	public String getBusinessUnit() {
		return businessUnit;
	}

	@JsonProperty("BusinessUnit")
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	@JsonProperty("City")
	public String getCity() {
		return city;
	}

	@JsonProperty("City")
	public void setCity(String city) {
		this.city = city;
	}

	@JsonProperty("ClientNumber")
	public String getClientNumber() {
		return clientNumber;
	}

	@JsonProperty("ClientNumber")
	public void setClientNumber(String clientNumber) {
		this.clientNumber = clientNumber;
	}

	@JsonProperty("ClientTracking")
	public String getClientTracking() {
		return clientTracking;
	}

	@JsonProperty("ClientTracking")
	public void setClientTracking(String clientTracking) {
		this.clientTracking = clientTracking;
	}

	@JsonProperty("County")
	public String getCounty() {
		return county;
	}

	@JsonProperty("County")
	public void setCounty(String county) {
		this.county = county;
	}

	@JsonProperty("DayLightSaving")
	public Boolean getDayLightSaving() {
		return dayLightSaving;
	}

	@JsonProperty("DayLightSaving")
	public void setDayLightSaving(Boolean dayLightSaving) {
		this.dayLightSaving = dayLightSaving;
	}

	@JsonProperty("FirmName")
	public String getFirmName() {
		return firmName;
	}

	@JsonProperty("FirmName")
	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	@JsonProperty("GeoCode")
	public String getGeoCode() {
		return geoCode;
	}

	@JsonProperty("GeoCode")
	public void setGeoCode(String geoCode) {
		this.geoCode = geoCode;
	}

	@JsonProperty("IsMatchLimitExceed")
	public Boolean getIsMatchLimitExceed() {
		return isMatchLimitExceed;
	}

	@JsonProperty("IsMatchLimitExceed")
	public void setIsMatchLimitExceed(Boolean isMatchLimitExceed) {
		this.isMatchLimitExceed = isMatchLimitExceed;
	}

	@JsonProperty("Latitude")
	public Double getLatitude() {
		return latitude;
	}

	@JsonProperty("Latitude")
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@JsonProperty("Longitude")
	public Double getLongitude() {
		return longitude;
	}

	@JsonProperty("Longitude")
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@JsonProperty("Message")
	public Object getMessage() {
		return message;
	}

	@JsonProperty("Message")
	public void setMessage(String message) {
		this.message = message;
	}

	@JsonProperty("PrimaryAddressLine")
	public String getPrimaryAddressLine() {
		return primaryAddressLine;
	}

	@JsonProperty("PrimaryAddressLine")
	public void setPrimaryAddressLine(String primaryAddressLine) {
		this.primaryAddressLine = primaryAddressLine;
	}

	@JsonProperty("ResultList")
	public List<String> getResultList() {
		return resultList;
	}

	@JsonProperty("ResultList")
	public void setResultList(List<String> resultList) {
		this.resultList = resultList;
	}

	@JsonProperty("ReturnCode")
	public Integer getReturnCode() {
		return returnCode;
	}

	@JsonProperty("ReturnCode")
	public void setReturnCode(Integer returnCode) {
		this.returnCode = returnCode;
	}

	@JsonProperty("Score")
	public Double getScore() {
		return score;
	}

	@JsonProperty("Score")
	public void setScore(Double score) {
		this.score = score;
	}

	@JsonProperty("SecondaryAddressLine")
	public String getSecondaryAddressLine() {
		return secondaryAddressLine;
	}

	@JsonProperty("SecondaryAddressLine")
	public void setSecondaryAddressLine(String secondaryAddressLine) {
		this.secondaryAddressLine = secondaryAddressLine;
	}

	@JsonProperty("State")
	public String getState() {
		return state;
	}

	@JsonProperty("State")
	public void setState(String state) {
		this.state = state;
	}

	@JsonProperty("TimeZone")
	public Integer getTimeZone() {
		return timeZone;
	}

	@JsonProperty("TimeZone")
	public void setTimeZone(Integer timeZone) {
		this.timeZone = timeZone;
	}

	@JsonProperty("TopMatchUnique")
	public Boolean getTopMatchUnique() {
		return topMatchUnique;
	}

	@JsonProperty("TopMatchUnique")
	public void setTopMatchUnique(Boolean topMatchUnique) {
		this.topMatchUnique = topMatchUnique;
	}

	@JsonProperty("ZIPCode")
	public String getZIPCode() {
		return zIPCode;
	}

	@JsonProperty("ZIPCode")
	public void setZIPCode(String zIPCode) {
		this.zIPCode = zIPCode;
	}

	@JsonProperty("ZIPPlus4")
	public String getZIPPlus4() {
		return zIPPlus4;
	}

	@JsonProperty("ZIPPlus4")
	public void setZIPPlus4(String zIPPlus4) {
		this.zIPPlus4 = zIPPlus4;
	}

}
