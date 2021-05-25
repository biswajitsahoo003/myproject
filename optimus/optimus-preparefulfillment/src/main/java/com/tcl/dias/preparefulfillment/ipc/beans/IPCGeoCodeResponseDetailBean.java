package com.tcl.dias.preparefulfillment.ipc.beans;

import java.io.Serializable;

public class IPCGeoCodeResponseDetailBean implements Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 private String City;
	 private String County;
	 private boolean DayLightSaving;
	 private String FirmName;
	 private String GeoCode;
	 private boolean IsMatchLimitExceed;
	 private Integer Latitude;
	 private Integer Longitude;
	 private String PrimaryAddressLine;
	 private Integer ReturnCode;
	 private Integer Score;
	 private String SecondaryAddressLine;
	 private String State;
	 private Integer TimeZone;
	 private boolean TopMatchUnique;
	 private String ZIPCode;
	 private String ZIPPlus4;
	 private String Country;
	 
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getCounty() {
		return County;
	}
	public void setCounty(String county) {
		County = county;
	}
	public boolean isDayLightSaving() {
		return DayLightSaving;
	}
	public void setDayLightSaving(boolean dayLightSaving) {
		DayLightSaving = dayLightSaving;
	}
	public String getFirmName() {
		return FirmName;
	}
	public void setFirmName(String firmName) {
		FirmName = firmName;
	}
	public String getGeoCode() {
		return GeoCode;
	}
	public void setGeoCode(String geoCode) {
		GeoCode = geoCode;
	}
	public boolean isIsMatchLimitExceed() {
		return IsMatchLimitExceed;
	}
	public void setIsMatchLimitExceed(boolean isMatchLimitExceed) {
		IsMatchLimitExceed = isMatchLimitExceed;
	}
	public Integer getLatitude() {
		return Latitude;
	}
	public void setLatitude(Integer latitude) {
		Latitude = latitude;
	}
	public Integer getLongitude() {
		return Longitude;
	}
	public void setLongitude(Integer longitude) {
		Longitude = longitude;
	}
	public String getPrimaryAddressLine() {
		return PrimaryAddressLine;
	}
	public void setPrimaryAddressLine(String primaryAddressLine) {
		PrimaryAddressLine = primaryAddressLine;
	}
	public Integer getReturnCode() {
		return ReturnCode;
	}
	public void setReturnCode(Integer returnCode) {
		ReturnCode = returnCode;
	}
	public Integer getScore() {
		return Score;
	}
	public void setScore(Integer score) {
		Score = score;
	}
	public String getSecondaryAddressLine() {
		return SecondaryAddressLine;
	}
	public void setSecondaryAddressLine(String secondaryAddressLine) {
		SecondaryAddressLine = secondaryAddressLine;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public Integer getTimeZone() {
		return TimeZone;
	}
	public void setTimeZone(Integer timeZone) {
		TimeZone = timeZone;
	}
	public boolean isTopMatchUnique() {
		return TopMatchUnique;
	}
	public void setTopMatchUnique(boolean topMatchUnique) {
		TopMatchUnique = topMatchUnique;
	}
	public String getZIPCode() {
		return ZIPCode;
	}
	public void setZIPCode(String zIPCode) {
		ZIPCode = zIPCode;
	}
	public String getZIPPlus4() {
		return ZIPPlus4;
	}
	public void setZIPPlus4(String zIPPlus4) {
		ZIPPlus4 = zIPPlus4;
	}
	public String getCountry() {
		return Country;
	}
	public void setCountry(String country) {
		Country = country;
	}
	
	@Override
	public String toString() {
		return "IPCGeoCodeResponseDetailBean [City=" + City + ", County=" + County + ", DayLightSaving="
				+ DayLightSaving + ", FirmName=" + FirmName + ", GeoCode=" + GeoCode + ", IsMatchLimitExceed="
				+ IsMatchLimitExceed + ", Latitude=" + Latitude + ", Longitude=" + Longitude + ", PrimaryAddressLine="
				+ PrimaryAddressLine + ", ReturnCode=" + ReturnCode + ", Score=" + Score + ", SecondaryAddressLine="
				+ SecondaryAddressLine + ", State=" + State + ", TimeZone=" + TimeZone + ", TopMatchUnique="
				+ TopMatchUnique + ", ZIPCode=" + ZIPCode + ", ZIPPlus4=" + ZIPPlus4 + ", Country=" + Country + "]";
	}
	 
}
