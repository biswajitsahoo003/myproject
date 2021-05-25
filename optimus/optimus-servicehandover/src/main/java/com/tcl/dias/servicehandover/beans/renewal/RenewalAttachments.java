package com.tcl.dias.servicehandover.beans.renewal;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.RenewalCommercialVettingDetails;

public class RenewalAttachments {
	private List<RenewalCommercialVettingDetails> renewalCommercialVettingDetails;

	public List<RenewalCommercialVettingDetails> getRenewalCommercialVettingDetails() {
		return renewalCommercialVettingDetails;
	}

	public void setRenewalCommercialVettingDetails(
			List<RenewalCommercialVettingDetails> renewalCommercialVettingDetails) {
		this.renewalCommercialVettingDetails = renewalCommercialVettingDetails;
	}

}
