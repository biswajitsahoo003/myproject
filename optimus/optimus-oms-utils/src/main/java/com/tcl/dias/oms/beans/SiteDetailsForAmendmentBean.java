package com.tcl.dias.oms.beans;

public class SiteDetailsForAmendmentBean {

    private int siteId;
    private int locationId;

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    @Override
    public String toString() {
        return "SiteDetailsForAmendmentBean{" +
                "siteId=" + siteId +
                ", locationId=" + locationId +
                '}';
    }
}
