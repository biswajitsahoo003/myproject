package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;
import java.util.List;

public class SdwanSitePerformanceDetails implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String siteName;
	
	private String serviceId;
	
	private List<SdwanCpePerformanceDetails> sdwanCpeDetails;
	
	
	
	public List<SdwanCpePerformanceDetails> getSdwanCpeDetails() {
		return sdwanCpeDetails;
	}
	public void setSdwanCpeDetails(List<SdwanCpePerformanceDetails> sdwanCpeDetails) {
		this.sdwanCpeDetails = sdwanCpeDetails;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	
	
}
