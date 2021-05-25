package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This file contains the Gvpn International custom feasibility attributes
 *
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GvpnIntlCustomFeasibilityRequest {

    private String type;
    private String accessType;
    private String feasibilityStatus;
    private String sfdcFeasibilityId;
    private String providerName;
    private String recordType;
    private String tclPOPAddress;
    private String tclPOPSiteCode;
    private String providerProductName;
    private String customerLatLong;
    private String localLoopCapacity;
    private String localLoopInterface;
    private String interConnectionType;
    private String contractTermWithVendor;
    private String lmCurrency;
    private String lmBwMRC;
    private String otcNrcInstallation;
    private String xConnectCurrency;
    private String xConnectMRC;
    private String xConnectNRC;
    private String remarks;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getFeasibilityStatus() {
        return feasibilityStatus;
    }

    public void setFeasibilityStatus(String feasibilityStatus) {
        this.feasibilityStatus = feasibilityStatus;
    }

    public String getSfdcFeasibilityId() {
        return sfdcFeasibilityId;
    }

    public void setSfdcFeasibilityId(String sfdcFeasibilityId) {
        this.sfdcFeasibilityId = sfdcFeasibilityId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getTclPOPAddress() {
        return tclPOPAddress;
    }

    public void setTclPOPAddress(String tclPOPAddress) {
        this.tclPOPAddress = tclPOPAddress;
    }

    public String getTclPOPSiteCode() {
        return tclPOPSiteCode;
    }

    public void setTclPOPSiteCode(String tclPOPSiteCode) {
        this.tclPOPSiteCode = tclPOPSiteCode;
    }

    public String getProviderProductName() {
        return providerProductName;
    }

    public void setProviderProductName(String providerProductName) {
        this.providerProductName = providerProductName;
    }

    public String getCustomerLatLong() {
        return customerLatLong;
    }

    public void setCustomerLatLong(String customerLatLong) {
        this.customerLatLong = customerLatLong;
    }

    public String getLocalLoopCapacity() {
        return localLoopCapacity;
    }

    public void setLocalLoopCapacity(String localLoopCapacity) {
        this.localLoopCapacity = localLoopCapacity;
    }

    public String getLocalLoopInterface() {
        return localLoopInterface;
    }

    public void setLocalLoopInterface(String localLoopInterface) {
        this.localLoopInterface = localLoopInterface;
    }

    public String getInterConnectionType() {
        return interConnectionType;
    }

    public void setInterConnectionType(String interConnectionType) {
        this.interConnectionType = interConnectionType;
    }

    public String getContractTermWithVendor() {
        return contractTermWithVendor;
    }

    public void setContractTermWithVendor(String contractTermWithVendor) {
        this.contractTermWithVendor = contractTermWithVendor;
    }

    public String getLmCurrency() {
        return lmCurrency;
    }

    public void setLmCurrency(String lmCurrency) {
        this.lmCurrency = lmCurrency;
    }

    public String getLmBwMRC() {
        return lmBwMRC;
    }

    public void setLmBwMRC(String lmBwMRC) {
        this.lmBwMRC = lmBwMRC;
    }

    public String getOtcNrcInstallation() {
        return otcNrcInstallation;
    }

    public void setOtcNrcInstallation(String otcNrcInstallation) {
        this.otcNrcInstallation = otcNrcInstallation;
    }

    public String getxConnectCurrency() {
        return xConnectCurrency;
    }

    public void setxConnectCurrency(String xConnectCurrency) {
        this.xConnectCurrency = xConnectCurrency;
    }

    public String getxConnectMRC() {
        return xConnectMRC;
    }

    public void setxConnectMRC(String xConnectMRC) {
        this.xConnectMRC = xConnectMRC;
    }

    public String getxConnectNRC() {
        return xConnectNRC;
    }

    public void setxConnectNRC(String xConnectNRC) {
        this.xConnectNRC = xConnectNRC;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
