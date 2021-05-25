package com.tcl.dias.location.beans;

 
/**
 * This class to return list of location ids and profile/offering names for multiple excel upload
 * @author NAVEEN GUNASEKARAN
 *
 */
public class LocationMultipleResponse {

	private Integer locationId;
	
	private Integer localItContactId;
	
	private String offeringName;

	public Integer getLocationId() {
		return locationId;
	}
	
	

	/**
	 * @return the localItContactId
	 */
	public Integer getLocalItContactId() {
		return localItContactId;
	}



	public String getOfferingName() {
		return offeringName;
	}



	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
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
