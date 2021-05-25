package com.tcl.dias.servicefulfillmentutils.beans;

public class TestEmailRequestPayload {
    private String originatorName;
    private String pmContactEmail;
    private String opOrderCode;
    private String orderType;
    private String scenarioType;
    private String originatorGroupId;
    private String orderCreationDate;
    private String orderEndDate;
    private String templateId;
    private String subject;

    public String getOriginatorName() {
        return originatorName;
    }

    public void setOriginatorName(String originatorName) {
        this.originatorName = originatorName;
    }

    public String getPmContactEmail() {
        return pmContactEmail;
    }

    public void setPmContactEmail(String pmContactEmail) {
        this.pmContactEmail = pmContactEmail;
    }

    public String getOpOrderCode() {
        return opOrderCode;
    }

    public void setOpOrderCode(String opOrderCode) {
        this.opOrderCode = opOrderCode;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getScenarioType() {
        return scenarioType;
    }

    public void setScenarioType(String scenarioType) {
        this.scenarioType = scenarioType;
    }

    public String getOriginatorGroupId() {
        return originatorGroupId;
    }

    public void setOriginatorGroupId(String originatorGroupId) {
        this.originatorGroupId = originatorGroupId;
    }

    public String getOrderCreationDate() {
        return orderCreationDate;
    }

    public void setOrderCreationDate(String orderCreationDate) {
        this.orderCreationDate = orderCreationDate;
    }

    public String getOrderEndDate() {
        return orderEndDate;
    }

    public void setOrderEndDate(String orderEndDate) {
        this.orderEndDate = orderEndDate;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
