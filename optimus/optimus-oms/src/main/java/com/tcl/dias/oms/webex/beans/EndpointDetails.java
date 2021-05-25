package com.tcl.dias.oms.webex.beans;

/**
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class EndpointDetails {
    private SiteAddress siteAddress;
    private Integer locationId;
    private String contractType;
    private String endpointManagementType;

    public EndpointDetails() {
    }

    public SiteAddress getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(SiteAddress siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getEndpointManagementType() {
        return endpointManagementType;
    }

    public void setEndpointManagementType(String endpointManagementType) {
        this.endpointManagementType = endpointManagementType;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    @Override
    public String toString() {
        return "EndpointDetails{" + "siteAddress=" + siteAddress + ", locationId=" + locationId + ", contractType='"
                + contractType + '\'' + ", endpointManagementType='" + endpointManagementType + '\'' + '}';
    }
}
