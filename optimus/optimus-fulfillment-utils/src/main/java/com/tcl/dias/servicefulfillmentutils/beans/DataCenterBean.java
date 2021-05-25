package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;

public class DataCenterBean implements Serializable {
    private String distributionCenterName;
    private Integer sapStorageLocation;
    private String plant;
    private String distributionCenterState;
    private String distributionCenterAddress;

    public String getDistributionCenterName() {
        return distributionCenterName;
    }

    public void setDistributionCenterName(String distributionCenterName) {
        this.distributionCenterName = distributionCenterName;
    }

    public Integer getSapStorageLocation() {
        return sapStorageLocation;
    }

    public void setSapStorageLocation(Integer sapStorageLocation) {
        this.sapStorageLocation = sapStorageLocation;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getDistributionCenterState() {
        return distributionCenterState;
    }

    public void setDistributionCenterState(String distributionCenterState) {
        this.distributionCenterState = distributionCenterState;
    }
    public String getDistributionCenterAddress() {
        return distributionCenterAddress;
    }

    public void setDistributionCenterAddress(String distributionCenterAddress) {
        this.distributionCenterAddress = distributionCenterAddress;
    }

}
