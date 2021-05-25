package com.tcl.dias.servicefulfillmentutils.beans.teamsdr;

import java.io.Serializable;

/**
 * @author Syed Ali.
 * @createdAt 04/03/2021, Thursday, 16:35
 */
public class TeamsDRHandoverNotePdfBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String orderId;
    private String serviceId;
    private String orderType;
    private String orderCategory="";
    private String customerName;
    private String customerContactNo;
    private String customerEmailId;
    private String customerContactDetails;
    private String customerContractingEntity;
    private String productName;
    private String customerGstNumberAddress;
    private String serviceAvailability="";
    private String lastMileType;
    private String localLoopProvider;
    private String cpeManagement;
    private String cpeSerialNumbers;
    private String siteAddress;
    private String demarcationBuildingName;
    private String demarcationWing;
    private String demarcationFloor;
    private String demarcationRoom;
    private String billStartDate;
    private String billFreePeriod;
    private String portBandwidth;
    private String localLoopBandwidth;

    private String commissioningDate;

    private String cloudName;
    private String numberOfUsersProvisioned;
    private String numberOfCommittedUsers;
    private String serviceType;
    private String deemedAcceptanceApplicable;

    public TeamsDRHandoverNotePdfBean() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderCategory() {
        return orderCategory;
    }

    public void setOrderCategory(String orderCategory) {
        this.orderCategory = orderCategory;
    }

    public String getCustomerContractingEntity() {
        return customerContractingEntity;
    }

    public void setCustomerContractingEntity(String customerContractingEntity) {
        this.customerContractingEntity = customerContractingEntity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCustomerGstNumberAddress() {
        return customerGstNumberAddress;
    }

    public void setCustomerGstNumberAddress(String customerGstNumberAddress) {
        this.customerGstNumberAddress = customerGstNumberAddress;
    }

    public String getServiceAvailability() {
        return serviceAvailability;
    }

    public void setServiceAvailability(String serviceAvailability) {
        this.serviceAvailability = serviceAvailability;
    }

    public String getLastMileType() {
        return lastMileType;
    }

    public void setLastMileType(String lastMileType) {
        this.lastMileType = lastMileType;
    }

    public String getLocalLoopProvider() {
        return localLoopProvider;
    }

    public void setLocalLoopProvider(String localLoopProvider) {
        this.localLoopProvider = localLoopProvider;
    }

    public String getCpeManagement() {
        return cpeManagement;
    }

    public void setCpeManagement(String cpeManagement) {
        this.cpeManagement = cpeManagement;
    }

    public String getCpeSerialNumbers() {
        return cpeSerialNumbers;
    }

    public void setCpeSerialNumbers(String cpeSerialNumbers) {
        this.cpeSerialNumbers = cpeSerialNumbers;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getDemarcationBuildingName() {
        return demarcationBuildingName;
    }

    public void setDemarcationBuildingName(String demarcationBuildingName) {
        this.demarcationBuildingName = demarcationBuildingName;
    }

    public String getDemarcationWing() {
        return demarcationWing;
    }

    public void setDemarcationWing(String demarcationWing) {
        this.demarcationWing = demarcationWing;
    }

    public String getDemarcationFloor() {
        return demarcationFloor;
    }

    public void setDemarcationFloor(String demarcationFloor) {
        this.demarcationFloor = demarcationFloor;
    }

    public String getDemarcationRoom() {
        return demarcationRoom;
    }

    public void setDemarcationRoom(String demarcationRoom) {
        this.demarcationRoom = demarcationRoom;
    }

    public String getBillStartDate() {
        return billStartDate;
    }

    public void setBillStartDate(String billStartDate) {
        this.billStartDate = billStartDate;
    }

    public String getBillFreePeriod() {
        return billFreePeriod;
    }

    public void setBillFreePeriod(String billFreePeriod) {
        this.billFreePeriod = billFreePeriod;
    }

    public String getPortBandwidth() {
        return portBandwidth;
    }

    public void setPortBandwidth(String portBandwidth) {
        this.portBandwidth = portBandwidth;
    }

    public String getLocalLoopBandwidth() {
        return localLoopBandwidth;
    }

    public void setLocalLoopBandwidth(String localLoopBandwidth) {
        this.localLoopBandwidth = localLoopBandwidth;
    }

    public String getCommissioningDate() {
        return commissioningDate;
    }

    public void setCommissioningDate(String commissioningDate) {
        this.commissioningDate = commissioningDate;
    }

    public String getCloudName() {
        return cloudName;
    }

    public void setCloudName(String cloudName) {
        this.cloudName = cloudName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerContactNo() {
        return customerContactNo;
    }

    public void setCustomerContactNo(String customerContactNo) {
        this.customerContactNo = customerContactNo;
    }

    public String getCustomerEmailId() {
        return customerEmailId;
    }

    public void setCustomerEmailId(String customerEmailId) {
        this.customerEmailId = customerEmailId;
    }

    public String getNumberOfUsersProvisioned() {
        return numberOfUsersProvisioned;
    }

    public void setNumberOfUsersProvisioned(String numberOfUsersProvisioned) {
        this.numberOfUsersProvisioned = numberOfUsersProvisioned;
    }

    public String getNumberOfCommittedUsers() {
        return numberOfCommittedUsers;
    }

    public void setNumberOfCommittedUsers(String numberOfCommittedUsers) {
        this.numberOfCommittedUsers = numberOfCommittedUsers;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDeemedAcceptanceApplicable() {
        return deemedAcceptanceApplicable;
    }

    public void setDeemedAcceptanceApplicable(String deemedAcceptanceApplicable) {
        this.deemedAcceptanceApplicable = deemedAcceptanceApplicable;
    }

    public String getCustomerContactDetails() {
        return customerContactDetails;
    }

    public void setCustomerContactDetails(String customerContactDetails) {
        this.customerContactDetails = customerContactDetails;
    }
}
