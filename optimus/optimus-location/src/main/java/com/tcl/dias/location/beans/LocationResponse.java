package com.tcl.dias.location.beans;

/**
 * This file contains the LocationResponse.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class LocationResponse {

	private Integer locationId;
	
	private Integer localItContactId;

	public Integer getLocationId() {
		return locationId;
	}
	
	

	/**
	 * @return the localItContactId
	 */
	public Integer getLocalItContactId() {
		return localItContactId;
	}



	/**
	 * @param localItContactId the localItContactId to set
	 */
	public void setLocalItContactId(Integer localItContactId) {
		this.localItContactId = localItContactId;
	}



	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	@Override
	public String toString() {
		return "LocationResponse [locationId=" + locationId + "]";
	}

}
