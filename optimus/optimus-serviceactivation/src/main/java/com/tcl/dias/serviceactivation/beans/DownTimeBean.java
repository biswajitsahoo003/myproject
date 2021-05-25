package com.tcl.dias.serviceactivation.beans;

import java.io.Serializable;

public class DownTimeBean implements Serializable {
	
	private static final long serialVersionUID = -1900703592960999669L;
	
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
