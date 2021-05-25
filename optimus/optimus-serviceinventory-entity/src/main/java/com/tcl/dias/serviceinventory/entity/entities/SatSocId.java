package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;

public class SatSocId implements Serializable {

    private String orderId;
    private String serviceId;

    public SatSocId(String orderId, String serviceId) {
        super();
        this.orderId = orderId;
        this.serviceId = serviceId;
    }
}
