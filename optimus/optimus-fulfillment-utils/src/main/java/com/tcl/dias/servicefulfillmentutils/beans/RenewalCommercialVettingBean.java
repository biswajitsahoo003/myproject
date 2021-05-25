package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class RenewalCommercialVettingBean extends BaseRequest{

    private List<RenewalCommercialVettingDetails> renewalCommercialVettingDetails;

	public List<RenewalCommercialVettingDetails> getRenewalCommercialVettingDetails() {
		return renewalCommercialVettingDetails;
	}

	public void setRenewalCommercialVettingDetails(List<RenewalCommercialVettingDetails> renewalCommercialVettingDetails) {
		this.renewalCommercialVettingDetails = renewalCommercialVettingDetails;
	}

    
}
