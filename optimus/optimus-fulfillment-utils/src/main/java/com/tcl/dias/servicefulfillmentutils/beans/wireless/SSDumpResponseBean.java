package com.tcl.dias.servicefulfillmentutils.beans.wireless;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SSDumpResponseBean {

    @JsonProperty("Response")
    private SSDumpBean response;

    public SSDumpBean getResponse() {
        return response;
    }

    public void setResponse(SSDumpBean response) {
        this.response = response;
    }

}
