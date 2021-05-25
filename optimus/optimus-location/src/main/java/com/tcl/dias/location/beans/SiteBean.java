package com.tcl.dias.location.beans;


/**
 * This file contains the site information for IZOPC
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SiteBean {
	
	Integer siteId;
	
	Integer locationId;
	
	String dataCenterCode;
	
	String latLong;

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getDataCenterCode() {
		return dataCenterCode;
	}

	public void setDataCenterCode(String dataCenterCode) {
		this.dataCenterCode = dataCenterCode;
	}

	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}
	
	

}
