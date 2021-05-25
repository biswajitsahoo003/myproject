package com.tcl.dias.common.serviceinventory.bean;

/*
    File Name : ScSolutionComponentBean.java
    
    @author Mayank Sharma
    First Created on 23-11-2020 at 19:46
*/

public class ScSolutionComponentBean {

    private String solutionCode;
    private String serviceCode;
    private String componentGroup;
    private String parentServiceCode;
    private String isActive;
    private String o2cTriggeredStatus;
    private String orderCode;
    private String priority;

    public String getSolutionCode() {
        return solutionCode;
    }

    public void setSolutionCode(String solutionCode) {
        this.solutionCode = solutionCode;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getComponentGroup() {
        return componentGroup;
    }

    public void setComponentGroup(String componentGroup) {
        this.componentGroup = componentGroup;
    }

    public String getParentServiceCode() {
        return parentServiceCode;
    }

    public void setParentServiceCode(String parentServiceCode) {
        this.parentServiceCode = parentServiceCode;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getO2cTriggeredStatus() {
        return o2cTriggeredStatus;
    }

    public void setO2cTriggeredStatus(String o2cTriggeredStatus) {
        this.o2cTriggeredStatus = o2cTriggeredStatus;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

}
