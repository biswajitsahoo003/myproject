package com.tcl.dias.serviceinventory.beans;

/**
 * Bean class to hold numbers for configurations
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SINumberConfigurationBean {

    private Integer assetId;

    private String assetType;

    private SIOrderNumberBean attributes;

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public SIOrderNumberBean getAttributes() {
        return attributes;
    }

    public void setAttributes(SIOrderNumberBean attributes) {
        this.attributes = attributes;
    }
}
