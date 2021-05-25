package com.tcl.dias.location.beans;

import io.swagger.models.auth.In;
/**
 * 
 * @author chetchau
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class LocationResponseBeanUsingPopAddresssId {
	private Integer locationId;
	private String latLong;
	private String locationAddress;
	private String locationType;
	private String locationEnd;
	public String getLocationEnd() {
		return locationEnd;
	}
	public void setLocationEnd(String locationEnd) {
		this.locationEnd = locationEnd;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	public Integer getLocationId() {
		return locationId;
	}
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	public String getLatLong() {
		return latLong;
	}
	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}
	public String getLocationAddress() {
		return locationAddress;
	}
	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}
	
	

}
