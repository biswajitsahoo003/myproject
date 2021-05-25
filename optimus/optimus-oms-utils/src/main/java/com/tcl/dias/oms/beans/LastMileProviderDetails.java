package com.tcl.dias.oms.beans;

import java.util.List;

public class LastMileProviderDetails {

    private int siteId;
    List<VendorDetails> vendorDetails;

    public List<VendorDetails> getVendorDetails() {
        return vendorDetails;
    }

    public void setVendorDetails(List<VendorDetails> vendorDetails) {
        this.vendorDetails = vendorDetails;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }



    @Override
    public String toString() {
        return "LastMileProviderDetails{" +
                "siteId=" + siteId +
                ", vendorDetails=" + vendorDetails +
                '}';
    }
}
