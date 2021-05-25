package com.tcl.dias.oms.beans;


import org.springframework.lang.NonNull;

/** 
 * This file contains the details for the order site update request
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * 
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OrderSiteRequest {
	
	@NonNull
	private String mstOrderSiteStatusName;
	
	
	private String mstOrderSiteStageName;

	
	/**
	 * 
	 * getMstOrderSiteStatusName
	 * @return the mstOrderSiteStatusName 
	 */
	public String getMstOrderSiteStatusName() {
		return mstOrderSiteStatusName;
	}

	
	/**
	 * 
	 * setMstOrderSiteStatusName
	 * @param mstOrderSiteStatusName the mstOrderSiteStatusName to set
	 */
	public void setMstOrderSiteStatusName(String mstOrderSiteStatusName) {
		this.mstOrderSiteStatusName = mstOrderSiteStatusName;
	}
	
	/**
	 * 
	 * getMstOrderSiteStageName
	 * @return the mstOrderSiteStageName
	 */

	public String getMstOrderSiteStageName() {
		return mstOrderSiteStageName;
	}
	
	/**
	 * 
	 * setMstOrderSiteStageName
	 * @param mstOrderSiteStageName the mstOrderSiteStageName to set
	 */

	public void setMstOrderSiteStageName(String mstOrderSiteStageName) {
		this.mstOrderSiteStageName = mstOrderSiteStageName;
	}
	
	

}
