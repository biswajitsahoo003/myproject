package com.tcl.dias.serviceinventory.beans;


import java.io.Serializable;
import java.util.List;

/**
 * @author archchan
 *
 */
public class SdwanSiteDetailsPerformaceBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<SdwanSitePerformanceDetails> siteDetails;
		
	
	public List<SdwanSitePerformanceDetails> getSiteDetails() {
		return siteDetails;
	}
	public void setSiteDetails(List<SdwanSitePerformanceDetails> siteDetails) {
		this.siteDetails = siteDetails;
	}
	}
