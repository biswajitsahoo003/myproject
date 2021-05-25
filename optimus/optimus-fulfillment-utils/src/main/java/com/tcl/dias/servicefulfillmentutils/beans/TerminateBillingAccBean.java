package com.tcl.dias.servicefulfillmentutils.beans;

public class TerminateBillingAccBean extends TaskDetailsBaseBean {
    private String isTerminatedInLR;

    public String getIsTerminatedInLR() {
        return isTerminatedInLR;
    }

    public void setIsTerminatedInLR(String isTerminatedInLR) {
        this.isTerminatedInLR = isTerminatedInLR;
    }

    @Override
    public String toString() {
        return "TerminateBillingAccBean{" +
                "isTerminatedInLR='" + isTerminatedInLR + '\'' +
                '}';
    }
}
