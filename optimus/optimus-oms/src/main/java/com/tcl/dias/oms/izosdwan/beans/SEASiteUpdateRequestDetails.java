package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;
import java.util.List;

public class SEASiteUpdateRequestDetails implements Serializable{
	private List<SEASiteDetailsBean> siteDetails;
	private String breakupLocation;
	public List<SEASiteDetailsBean> getSiteDetails() {
		return siteDetails;
	}
	public void setSiteDetails(List<SEASiteDetailsBean> siteDetails) {
		this.siteDetails = siteDetails;
	}
	public String getBreakupLocation() {
		return breakupLocation;
	}
	public void setBreakupLocation(String breakupLocation) {
		this.breakupLocation = breakupLocation;
	}
	
	

}
