package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;

public class DownTimeBean implements Serializable {
    private String serviceId;
    private String requestID;
    private String requestingSystem;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getRequestingSystem() {
        return requestingSystem;
    }

    public void setRequestingSystem(String requestingSystem) {
        this.requestingSystem = requestingSystem;
    }

    @Override
    public String toString() {
        return "DownTimeBean{" +
                "serviceId='" + serviceId + '\'' +
                ", requestID='" + requestID + '\'' +
                ", requestingSystem='" + requestingSystem + '\'' +
                '}';
    }
}
