package com.tcl.dias.common.serviceinventory.beans;

import java.io.Serializable;

/**
 * Bean class for sicontractinfo
 * @author archchan
 *
 */
public class SiServiceSiContractInfoBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String billingCurrency;
	private Double mrc;
	private String baseBandwidth;
	private String bandwidthUnit;
	
	public String getBillingCurrency() {
		return billingCurrency;
	}
	public void setBillingCurrency(String billingCurrency) {
		this.billingCurrency = billingCurrency;
	}
	public Double getMrc() {
		return mrc;
	}
	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}
	public String getBaseBandwidth() {
		return baseBandwidth;
	}
	public void setBaseBandwidth(String baseBandwidth) {
		this.baseBandwidth = baseBandwidth;
	}
	public String getBandwidthUnit() {
		return bandwidthUnit;
	}
	public void setBandwidthUnit(String bandwidthUnit) {
		this.bandwidthUnit = bandwidthUnit;
	}

}
