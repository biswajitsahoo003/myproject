package com.tcl.dias.servicefulfillment.beans.gsc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BillingProfileApiBean {
    private String profileRelNo;

    private BillingDetailsBean billingDetails;

    private BillingContactBean billingContact;

    private BillingAddressBean billingAddress;
    
    @JsonProperty("CustGSTNAddress")
    private CustGSTNAddressBean CustGSTNAddress;

    private String message;

    private String status;

    public String getProfileRelNo() {
        return profileRelNo;
    }

    public void setProfileRelNo(String profileRelNo) {
        this.profileRelNo = profileRelNo;
    }

    public BillingDetailsBean getBillingDetails() {
        return billingDetails;
    }

    public void setBillingDetails(BillingDetailsBean billingDetails) {
        this.billingDetails = billingDetails;
    }

    public BillingContactBean getBillingContact() {
        return billingContact;
    }

    public void setBillingContact(BillingContactBean billingContact) {
        this.billingContact = billingContact;
    }

    public BillingAddressBean getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddressBean billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public CustGSTNAddressBean getCustGSTNAddress() {
		return CustGSTNAddress;
	}

	public void setCustGSTNAddress(CustGSTNAddressBean custGSTNAddress) {
		CustGSTNAddress = custGSTNAddress;
	}
    
    
}
