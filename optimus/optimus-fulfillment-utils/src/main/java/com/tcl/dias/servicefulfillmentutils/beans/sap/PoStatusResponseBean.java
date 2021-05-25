package com.tcl.dias.servicefulfillmentutils.beans.sap;

public class PoStatusResponseBean {

    private PoStatusBean poStatusBean;

    public PoStatusBean getPoStatusBean() {
        return poStatusBean;
    }

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
