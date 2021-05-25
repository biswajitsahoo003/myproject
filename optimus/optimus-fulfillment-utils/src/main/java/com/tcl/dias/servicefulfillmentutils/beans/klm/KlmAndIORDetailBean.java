package com.tcl.dias.servicefulfillmentutils.beans.klm;

import java.util.List;

public class KlmAndIORDetailBean {

    private String serviceId;
    private String cloudName;

    private List<ReservedTimeSlotObjects> reservedTimeSlotObjects;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getCloudName() {
        return cloudName;
    }

    public void setCloudName(String cloudName) {
        this.cloudName = cloudName;
    }

    public List<ReservedTimeSlotObjects> getReservedTimeSlotObjects() {
        return reservedTimeSlotObjects;
    }

    public void setReservedTimeSlotObjects(List<ReservedTimeSlotObjects> reservedTimeSlotObjects) {
        this.reservedTimeSlotObjects = reservedTimeSlotObjects;
    }
}
