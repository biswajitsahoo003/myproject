package com.tcl.dias.networkaugmentation.beans;

import java.io.Serializable;
import java.sql.Timestamp;

public class NwaOrderDetailsBean implements Serializable {

    private Integer orderId;

    private String orderCode;

    private String orderType;

    private String btsOrderId;

    private String isIprmRequired;

    private String isPeRequired;

    private String mAndLRequired;

    private Timestamp orderCreationDate;

    private String orderStatus;

    private String originatOrContactNumber;

    private String originatorGroupId;

    private String originatorName;

    private String pmContactEmail;

    private String pmContactPhoneNo;

    private String pmName;

    private String priority;

    private String projectType;

    private String scenarioType;

    private  String technologyType;

    private String subject;

    private String serviceId;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getBtsOrderId() {
        return btsOrderId;
    }

    public void setBtsOrderId(String btsOrderId) {
        this.btsOrderId = btsOrderId;
    }

    public String getIsIprmRequired() {
        return isIprmRequired;
    }

    public void setIsIprmRequired(String isIprmRequired) {
        this.isIprmRequired = isIprmRequired;
    }

    public String getIsPeRequired() {
        return isPeRequired;
    }

    public void setIsPeRequired(String isPeRequired) {
        this.isPeRequired = isPeRequired;
    }

    public String getmAndLRequired() {
        return mAndLRequired;
    }

    public void setmAndLRequired(String mAndLRequired) {
        this.mAndLRequired = mAndLRequired;
    }

    public Timestamp getOrderCreationDate() {
        return orderCreationDate;
    }

    public void setOrderCreationDate(Timestamp orderCreationDate) {
        this.orderCreationDate = orderCreationDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOriginatOrContactNumber() {
        return originatOrContactNumber;
    }

    public void setOriginatOrContactNumber(String originatOrContactNumber) {
        this.originatOrContactNumber = originatOrContactNumber;
    }

    public String getOriginatorGroupId() {
        return originatorGroupId;
    }

    public void setOriginatorGroupId(String originatorGroupId) {
        this.originatorGroupId = originatorGroupId;
    }

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

    public String getPmContactPhoneNo() {
        return pmContactPhoneNo;
    }

    public void setPmContactPhoneNo(String pmContactPhoneNo) {
        this.pmContactPhoneNo = pmContactPhoneNo;
    }

    public String getPmName() {
        return pmName;
    }

    public void setPmName(String pmName) {
        this.pmName = pmName;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getScenarioType() {
        return scenarioType;
    }

    public void setScenarioType(String scenarioType) {
        this.scenarioType = scenarioType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getTechnologyType() {
        return technologyType;
    }

    public void setTechnologyType(String technologyType) {
        this.technologyType = technologyType;
    }
}
