package com.tcl.dias.serviceinventory.beans;

import java.util.List;
/**
* Bean for storing source and destination addresses
* @author Srinivasa Raghavan
*/
public class SdwanAddressBean {
    private String addressName;
    private String ipAddress;

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
