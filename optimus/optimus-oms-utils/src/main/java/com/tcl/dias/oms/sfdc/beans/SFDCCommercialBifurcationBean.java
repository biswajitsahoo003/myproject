package com.tcl.dias.oms.sfdc.beans;

public class SFDCCommercialBifurcationBean {

    private Double mrc = 0D;
    private Double nrc = 0D;

    public Double getMrc() {
        return mrc;
    }

    public void setMrc(Double mrc) {
        this.mrc = mrc;
    }

    public Double getNrc() {
        return nrc;
    }

    public void setNrc(Double nrc) {
        this.nrc = nrc;
    }

    @Override
    public String toString() {
        return "SFDCCommercialBifurcationBean{" +
                "mrc=" + mrc +
                ", nrc=" + nrc +
                '}';
    }
}
