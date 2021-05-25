package com.tcl.dias.oms.beans;

import java.util.List;

public class SiteToServiceMapping {

    Integer siteId;
    List<ServiceDetailsForASite> details;

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public List<ServiceDetailsForASite> getDetails() {
        return details;
    }

    public void setDetails(List<ServiceDetailsForASite> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "SiteToServiceMapping{" +
                "siteId=" + siteId +
                ", details=" + details +
                '}';
    }
}
