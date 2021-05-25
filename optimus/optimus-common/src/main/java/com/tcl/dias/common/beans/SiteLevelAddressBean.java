package com.tcl.dias.common.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * This file contains the SiteLevelAddressBean.java class.
 * 
 *
 * @author Nithya S
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
public class SiteLevelAddressBean {

	private List<GstAddressInfo> gstAddressInfo;
	
	private List<BillingAddressInfo> billingAddressInfo;
	
	private List<LocationAddressInfo> locationAddressInfo;
	
	private Integer legalId;
	
	public List<GstAddressInfo> getGstAddressInfo() {
		return gstAddressInfo;
	}

	public void setGstAddressInfo(List<GstAddressInfo> gstAddressInfo) {
		this.gstAddressInfo = gstAddressInfo;
	}

	public List<BillingAddressInfo> getBillingAddressInfo() {
		return billingAddressInfo;
	}

	public void setBillingAddressInfo(List<BillingAddressInfo> billingAddressInfo) {
		this.billingAddressInfo = billingAddressInfo;
	}

	public List<LocationAddressInfo> getLocationAddressInfo() {
		return locationAddressInfo;
	}

	public void setLocationAddressInfo(List<LocationAddressInfo> locationAddressInfo) {
		this.locationAddressInfo = locationAddressInfo;
	}

	public Integer getLegalId() {
		return legalId;
	}

	public void setLegalId(Integer legalId) {
		this.legalId = legalId;
	}

	@Override
	public String toString() {
		return "SiteLevelAddressBean [gstAddressInfo=" + gstAddressInfo + ", billingAddressInfo=" + billingAddressInfo
				+ ", locationAddressInfo=" + locationAddressInfo + ", legalId=" + legalId + "]";
	}


}
