package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationBean{
		
	private String cityAbbr;
	private String countryAbbr;
	private String latitudeDegree;
	private String latitudeMinutes;
	private String latitudeSeconds;
	private String latitudeDirection;
	private String longitudeDegree;
	private String longitudeMinutes;
	private String longitudeSeconds;
	private String longitudeDirection;
	
	public String getCityAbbr() {
		return cityAbbr;
	} 
	
	public void setCityAbbr(String cityAbbr) {
		this.cityAbbr = cityAbbr;
	}
	
	public String getCountryAbbr() {
		return countryAbbr;
	}
	
	public void setCountryAbbr(String countryAbbr) {
		this.countryAbbr = countryAbbr;
	}
	
	public String getLatitudeDegree() {
		return latitudeDegree;
	}
	
	public void setLatitudeDegree(String latitudeDegree) {
		this.latitudeDegree = latitudeDegree;
	}
	
	public String getLatitudeMinutes() {
		return latitudeMinutes;
	}
	
	public void setLatitudeMinutes(String latitudeMinutes) {
		this.latitudeMinutes = latitudeMinutes;
	}
	
	public String getLatitudeSeconds() {
		return latitudeSeconds;
	}
	
	public void setLatitudeSeconds(String latitudeSeconds) {
		this.latitudeSeconds = latitudeSeconds;
	}
	
	public String getLatitudeDirection() {
		return latitudeDirection;
	}
	
	public void setLatitudeDirection(String latitudeDirection) {
		this.latitudeDirection = latitudeDirection;
	}
	
	public String getLongitudeDegree() {
		return longitudeDegree;
	}
	
	public void setLongitudeDegree(String longitudeDegree) {
		this.longitudeDegree = longitudeDegree;
	}
	
	public String getLongitudeMinutes() {
		return longitudeMinutes;
	}
	
	public void setLongitudeMinutes(String longitudeMinutes) {
		this.longitudeMinutes = longitudeMinutes;
	}
	
	public String getLongitudeSeconds() {
		return longitudeSeconds;
	}
	
	public void setLongitudeSeconds(String longitudeSeconds) {
		this.longitudeSeconds = longitudeSeconds;
	}
	
	public String getLongitudeDirection() {
		return longitudeDirection;
	}
	
	public void setLongitudeDirection(String longitudeDirection) {
		this.longitudeDirection = longitudeDirection;
	}
	
}
