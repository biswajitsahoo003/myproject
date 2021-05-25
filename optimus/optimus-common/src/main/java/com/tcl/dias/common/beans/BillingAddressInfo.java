package com.tcl.dias.common.beans;

/**
 * This file contains the BillingAddressInfo.java class.
 * 
 *
 * @author Nithya S
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
public class BillingAddressInfo {

	private Integer multisiteInfoId;
	private Integer billingInfoId;
	private String billingAddress;
	
	public Integer getMultisiteInfoId() {
		return multisiteInfoId;
	}
	public void setMultisiteInfoId(Integer multisiteInfoId) {
		this.multisiteInfoId = multisiteInfoId;
	}
	public String getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}
	public Integer getBillingInfoId() {
		return billingInfoId;
	}
	public void setBillingInfoId(Integer billingInfoId) {
		this.billingInfoId = billingInfoId;
	}
	
	@Override
	public String toString() {
		return "BillingAddressInfo [multisiteInfoId=" + multisiteInfoId + ", billingInfoId=" + billingInfoId
				+ ", billingAddress=" + billingAddress + "]";
	}
	
}
