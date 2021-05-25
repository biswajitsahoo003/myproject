package com.tcl.dias.oms.beans;

public class ServiceDetailsForASite {
    String serviceId;
    String type;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SiteToServiceMappingBean{" +
                "serviceId='" + serviceId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
