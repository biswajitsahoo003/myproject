package com.tcl.dias.oms.webex.beans;

/**
 * Bean for having information about existing
 * GVPN from service inventory
 *
 * @author Srinivasa Raghavan
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class ExistingGVPNInfo {

    //Existing GVPN attributes
    private String serviceId;
    private String siteAddress;
    private String aliasName;
    private String bandwidth;
    private String locationId;

    public ExistingGVPNInfo() {
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    @Override
    public String toString() {
        return "ExistingGVPNInfo{" +
                "serviceId='" + serviceId + '\'' +
                ", siteAddress='" + siteAddress + '\'' +
                ", aliasName='" + aliasName + '\'' +
                ", bandwidth='" + bandwidth + '\'' +
                ", locationId='" + locationId + '\'' +
                '}';
    }
}
