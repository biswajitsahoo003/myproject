package com.tcl.dias.serviceactivation.beans;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class SdwanProvisionDetailsBean extends TaskDetailsBaseBean {
    private String linkProvisionStatus;
    private String legacyServiceId;
    private String ipAddress;

    public String getLinkProvisionStatus() {
        return linkProvisionStatus;
    }

    public void setLinkProvisionStatus(String linkProvisionStatus) {
        this.linkProvisionStatus = linkProvisionStatus;
    }

    public String getLegacyServiceId() {
        return legacyServiceId;
    }

    public void setLegacyServiceId(String legacyServiceId) {
        this.legacyServiceId = legacyServiceId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
