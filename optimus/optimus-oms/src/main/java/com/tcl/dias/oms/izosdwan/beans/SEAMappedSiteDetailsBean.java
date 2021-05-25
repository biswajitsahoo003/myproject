package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * This file contains the SEAMappedSiteDetailsBean.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SEAMappedSiteDetailsBean implements Serializable{
	
	private Integer totalSites;
	private String totalBw;
	private String breakupLocation;
	private List<SEASiteDetailsBean> locations;
	public Integer getTotalSites() {
		return totalSites;
	}
	public void setTotalSites(Integer totalSites) {
		this.totalSites = totalSites;
	}
	public String getTotalBw() {
		return totalBw;
	}
	public void setTotalBw(String totalBw) {
		this.totalBw = totalBw;
	}
	public String getBreakupLocation() {
		return breakupLocation;
	}
	public void setBreakupLocation(String breakupLocation) {
		this.breakupLocation = breakupLocation;
	}
	public List<SEASiteDetailsBean> getLocations() {
		return locations;
	}
	public void setLocations(List<SEASiteDetailsBean> locations) {
		this.locations = locations;
	}
	

}
