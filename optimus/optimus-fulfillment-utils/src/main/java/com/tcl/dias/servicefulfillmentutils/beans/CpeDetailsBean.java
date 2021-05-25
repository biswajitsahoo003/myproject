package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

public class CpeDetailsBean {

    private Integer componentId;
    private String cpeModel;
    private String partCode;
    private String bwSupported;
    private String l2Port;
    private String l3Port;
    private String cpeDeviceName;
    private String cpeType;
    private List<UnderlayDetails> details;
    private String cpeNetworkDetails;
//    private String templateName;

    public Integer getComponentId() {
        return componentId;
    }

    public void setComponentId(Integer componentId) {
        this.componentId = componentId;
    }

    public String getCpeModel() {
        return cpeModel;
    }

    public void setCpeModel(String cpeModel) {
        this.cpeModel = cpeModel;
    }

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public String getBwSupported() {
        return bwSupported;
    }

    public void setBwSupported(String bwSupported) {
        this.bwSupported = bwSupported;
    }

    public String getL3Port() {
        return l3Port;
    }

    public void setL3Port(String l3Port) {
        this.l3Port = l3Port;
    }

    public String getL2Port() {
        return l2Port;
    }

    public void setL2Port(String l2Port) {
        this.l2Port = l2Port;
    }

    public String getCpeDeviceName() {
        return cpeDeviceName;
    }

    public void setCpeDeviceName(String cpeDeviceName) {
        this.cpeDeviceName = cpeDeviceName;
    }

    public String getCpeType() {
        return cpeType;
    }

    public void setCpeType(String cpeType) {
        this.cpeType = cpeType;
    }

    public List<UnderlayDetails> getDetails() {
        return details;
    }

    public void setDetails(List<UnderlayDetails> details) {
        this.details = details;
    }

    public String getCpeNetworkDetails() {
        return cpeNetworkDetails;
    }

    public void setCpeNetworkDetails(String cpeNetworkDetails) {
        this.cpeNetworkDetails = cpeNetworkDetails;
    }

//    public String getTemplateName() {
//        return templateName;
//    }
//
//    public void setTemplateName(String templateName) {
//        this.templateName = templateName;
//    }
}
