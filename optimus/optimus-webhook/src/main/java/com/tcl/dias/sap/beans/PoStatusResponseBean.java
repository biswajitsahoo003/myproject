package com.tcl.dias.sap.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PoStatusResponseBean {

    @JsonProperty("PO_Response")
    private PoStatusBean poStatusBean;

    @JsonProperty("PO_Response")
    public PoStatusBean getPoStatusBean() {
        return poStatusBean;
    }

    @JsonProperty("PO_Response")
    public void setPoStatusBean(PoStatusBean poStatusBean) {
        this.poStatusBean = poStatusBean;
    }

    @Override
    public String toString() {
        return "PoStatusResponseBean{" +
                "poStatusBean=" + poStatusBean +
                '}';
    }
}
