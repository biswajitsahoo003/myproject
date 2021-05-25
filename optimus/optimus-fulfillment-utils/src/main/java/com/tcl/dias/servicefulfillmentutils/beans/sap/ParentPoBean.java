package com.tcl.dias.servicefulfillmentutils.beans.sap;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Bean class
 *
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public class ParentPoBean {

    private String tclServiceId;

    private String poNumber;

    private String childPoNumber;

    private String bsoCircuitId;

    private String productComponent;

    private String poCreationDate;

    private String vendorId;

    private String sfdcProviderName;

    private String vendorName;


    public String getTclServiceId() {
        return tclServiceId;
    }

    public void setTclServiceId(String tclServiceId) {
        this.tclServiceId = tclServiceId;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getBsoCircuitId() {
        return bsoCircuitId;
    }

    public void setBsoCircuitId(String bsoCircuitId) {
        this.bsoCircuitId = bsoCircuitId;
    }

    public String getProductComponent() {
        return productComponent;
    }

    public void setProductComponent(String productComponent) {
        this.productComponent = productComponent;
    }

    public String getPoCreationDate() {
        return poCreationDate;
    }

    public void setPoCreationDate(String poCreationDate) {
        this.poCreationDate = poCreationDate;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getSfdcProviderName() {
        return sfdcProviderName;
    }

    public void setSfdcProviderName(String sfdcProviderName) {
        this.sfdcProviderName = sfdcProviderName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getChildPoNumber() {
        return childPoNumber;
    }

    public void setChildPoNumber(String childPoNumber) {
        this.childPoNumber = childPoNumber;
    }

    @Override
    public String toString() {
        return "ParentPoBean{" +
                "tclServiceId='" + tclServiceId + '\'' +
                ", poNumber='" + poNumber + '\'' +
                ", childPoNumber='" + childPoNumber + '\'' +
                ", bsoCircuitId='" + bsoCircuitId + '\'' +
                ", productComponent='" + productComponent + '\'' +
                ", poCreationDate='" + poCreationDate + '\'' +
                ", vendorId='" + vendorId + '\'' +
                ", sfdcProviderName='" + sfdcProviderName + '\'' +
                ", vendorName='" + vendorName + '\'' +
                '}';
    }
}
