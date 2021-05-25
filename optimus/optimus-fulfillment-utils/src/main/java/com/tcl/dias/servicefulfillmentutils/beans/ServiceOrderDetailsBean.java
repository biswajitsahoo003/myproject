package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;

public class ServiceOrderDetailsBean implements Serializable {

    private String usageModel;
    private String serviceBandwidth;
    private String serviceBandwidthUnit;
    private String sfdcOpportunityId;
    private String optyBidCategory;

    public String getUsageModel() {
        return usageModel;
    }

    public void setUsageModel(String usageModel) {
        this.usageModel = usageModel;
    }

    public String getServiceBandwidth() {
        return serviceBandwidth;
    }

    public void setServiceBandwidth(String serviceBandwidth) {
        this.serviceBandwidth = serviceBandwidth;
    }

    public String getServiceBandwidthUnit() {
        return serviceBandwidthUnit;
    }

    public void setServiceBandwidthUnit(String serviceBandwidthUnit) {
        this.serviceBandwidthUnit = serviceBandwidthUnit;
    }

    public String getSfdcOpportunityId() {
        return sfdcOpportunityId;
    }

    public void setSfdcOpportunityId(String sfdcOpportunityId) {
        this.sfdcOpportunityId = sfdcOpportunityId;
    }

    public String getOptyBidCategory() {
        return optyBidCategory;
    }

    public void setOptyBidCategory(String optyBidCategory) {
        this.optyBidCategory = optyBidCategory;
    }
}
