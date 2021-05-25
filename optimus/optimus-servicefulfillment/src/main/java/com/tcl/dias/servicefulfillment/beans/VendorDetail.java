package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author diksha garg
 * 
 * VendorDetail for getting Vendor Master Data
 *
 ** @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class VendorDetail {

	private String vendorId;
    private String vendorName;
    private List<VendorContact> vendorContacts = null;
 
    public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public List<VendorContact> getVendorContacts() {
        return vendorContacts;
    }

    public void setVendorContacts(List<VendorContact> vendorContacts) {
        this.vendorContacts = vendorContacts;
    }
}
