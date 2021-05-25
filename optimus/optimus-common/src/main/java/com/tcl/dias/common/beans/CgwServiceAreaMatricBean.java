package com.tcl.dias.common.beans;

import java.io.Serializable;
/**
 * 
 * This file contains the CgwServiceAreaMatricBean.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CgwServiceAreaMatricBean implements Serializable{
	private String cityName;
	private String siteId;
	private String popAddress;
	private Integer locationId;
	private String primaryIor;
	private String secondaryIor;
	private String asno;
	private String hostName;
	private String serialNumber;
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getPopAddress() {
		return popAddress;
	}
	public void setPopAddress(String popAddress) {
		this.popAddress = popAddress;
	}
	public Integer getLocationId() {
		return locationId;
	}
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	public String getPrimaryIor() {
		return primaryIor;
	}
	public void setPrimaryIor(String primaryIor) {
		this.primaryIor = primaryIor;
	}
	public String getSecondaryIor() {
		return secondaryIor;
	}
	public void setSecondaryIor(String secondaryIor) {
		this.secondaryIor = secondaryIor;
	}
	public String getAsno() {
		return asno;
	}
	public void setAsno(String asno) {
		this.asno = asno;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
}
