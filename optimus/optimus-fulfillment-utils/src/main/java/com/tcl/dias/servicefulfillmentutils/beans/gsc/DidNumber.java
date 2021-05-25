package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DidNumber {

    private String correlationId;
    private String countryCode;
    private String didNumberWithoutCC;
    private Boolean isPortable;
    private String origDIDOwner;
    private Integer didCMS;
    private String invoiceGroupID;
    private String supplierId;
    private String custSiteAbbr;
    private Boolean emergencyEnabled;
    private Boolean addressSameAsCustSite;
    private String address;
    private String stateOrProvinceAbbr;
    private String postalCd;
    private Integer custToTataConnectionCMS;
    private Integer tataToCustConnectionId;
    private String dtgHeader;
    private String isCName;
    private String isCallerId;
    private String status;

    //below three properties are Patch Number response attributtes
    private String didNumberId;
    private String inServiceDate;
    private String statusMsg;

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDidNumberWithoutCC() {
        return didNumberWithoutCC;
    }

    public void setDidNumberWithoutCC(String didNumberWithoutCC) {
        this.didNumberWithoutCC = didNumberWithoutCC;
    }

    public Boolean getIsPortable() {
        return isPortable;
    }

    public void setIsPortable(Boolean portable) {
        isPortable = portable;
    }

    public String getOrigDIDOwner() {
        return origDIDOwner;
    }

    public void setOrigDIDOwner(String origDIDOwner) {
        this.origDIDOwner = origDIDOwner;
    }

    public Integer getDidCMS() {
        return didCMS;
    }

    public void setDidCMS(Integer didCMS) {
        this.didCMS = didCMS;
    }

    public String getInvoiceGroupID() {
        return invoiceGroupID;
    }

    public void setInvoiceGroupID(String invoiceGroupID) {
        this.invoiceGroupID = invoiceGroupID;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getCustSiteAbbr() {
        return custSiteAbbr;
    }

    public void setCustSiteAbbr(String custSiteAbbr) {
        this.custSiteAbbr = custSiteAbbr;
    }

    public Boolean getEmergencyEnabled() {
        return emergencyEnabled;
    }

    public void setEmergencyEnabled(Boolean emergencyEnabled) {
        this.emergencyEnabled = emergencyEnabled;
    }

    public Boolean getAddressSameAsCustSite() {
        return addressSameAsCustSite;
    }

    public void setAddressSameAsCustSite(Boolean addressSameAsCustSite) {
        this.addressSameAsCustSite = addressSameAsCustSite;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStateOrProvinceAbbr() {
        return stateOrProvinceAbbr;
    }

    public void setStateOrProvinceAbbr(String stateOrProvinceAbbr) {
        this.stateOrProvinceAbbr = stateOrProvinceAbbr;
    }

    public String getPostalCd() {
        return postalCd;
    }

    public void setPostalCd(String postalCd) {
        this.postalCd = postalCd;
    }

    public Integer getCustToTataConnectionCMS() {
        return custToTataConnectionCMS;
    }

    public void setCustToTataConnectionCMS(Integer custToTataConnectionCMS) {
        this.custToTataConnectionCMS = custToTataConnectionCMS;
    }

    public Integer getTataToCustConnectionId() {
        return tataToCustConnectionId;
    }

    public void setTataToCustConnectionId(Integer tataToCustConnectionId) {
        this.tataToCustConnectionId = tataToCustConnectionId;
    }

    public String getDtgHeader() {
        return dtgHeader;
    }

    public void setDtgHeader(String dtgHeader) {
        this.dtgHeader = dtgHeader;
    }

    public String getIsCName() {
        return isCName;
    }

    public void setIsCName(String isCName) {
        this.isCName = isCName;
    }

    public String getIsCallerId() {
        return isCallerId;
    }

    public void setIsCallerId(String isCallerId) {
        this.isCallerId = isCallerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDidNumberId() {
        return didNumberId;
    }

    public void setDidNumberId(String didNumberId) {
        this.didNumberId = didNumberId;
    }

    public String getInServiceDate() {
        return inServiceDate;
    }

    public void setInServiceDate(String inServiceDate) {
        this.inServiceDate = inServiceDate;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }
}
