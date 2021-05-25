package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;

public class BSODetailsBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String childPoNumber;

    private Timestamp etlLoadDt;

    private String parentPoNumber;

    private String poCreationDate;

    private String poLineNo;

    private String poNumber;

    private String productComponent;

    private String srcSystem;

    private String tclServiceId;

    private String terminationType;

    private String vendorNo;

    private String vendorRefIdOrderId;

    public String getChildPoNumber() {
        return childPoNumber;
    }

    public void setChildPoNumber(String childPoNumber) {
        this.childPoNumber = childPoNumber;
    }

    public Timestamp getEtlLoadDt() {
        return etlLoadDt;
    }

    public void setEtlLoadDt(Timestamp etlLoadDt) {
        this.etlLoadDt = etlLoadDt;
    }

    public String getParentPoNumber() {
        return parentPoNumber;
    }

    public void setParentPoNumber(String parentPoNumber) {
        this.parentPoNumber = parentPoNumber;
    }

    public String getPoCreationDate() {
        return poCreationDate;
    }

    public void setPoCreationDate(String poCreationDate) {
        this.poCreationDate = poCreationDate;
    }

    public String getPoLineNo() {
        return poLineNo;
    }

    public void setPoLineNo(String poLineNo) {
        this.poLineNo = poLineNo;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getProductComponent() {
        return productComponent;
    }

    public void setProductComponent(String productComponent) {
        this.productComponent = productComponent;
    }

    public String getSrcSystem() {
        return srcSystem;
    }

    public void setSrcSystem(String srcSystem) {
        this.srcSystem = srcSystem;
    }

    public String getTclServiceId() {
        return tclServiceId;
    }

    public void setTclServiceId(String tclServiceId) {
        this.tclServiceId = tclServiceId;
    }

    public String getTerminationType() {
        return terminationType;
    }

    public void setTerminationType(String terminationType) {
        this.terminationType = terminationType;
    }

    public String getVendorNo() {
        return vendorNo;
    }

    public void setVendorNo(String vendorNo) {
        this.vendorNo = vendorNo;
    }

    public String getVendorRefIdOrderId() {
        return vendorRefIdOrderId;
    }

    public void setVendorRefIdOrderId(String vendorRefIdOrderId) {
        this.vendorRefIdOrderId = vendorRefIdOrderId;
    }



}
