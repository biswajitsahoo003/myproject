package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class RenewalCommercialVettingDetails{

    private Integer serviceId;
    private String serviceCode;
    private String serviceVariant;
    private String cpeContractType;
    private String usageType;
    private String taxExemptionReason;
    private String additionalIp;
    private String crossConnectType;
    private String mediaType;
    private String fiberEntryType;
    private String fiberType;
    private String fiberPairNumber;
    private String cablePairNumber;

    private String totalArc;
    private String totalNrc;
    private String totalMrc;


    private List<AttachmentBean> attachmentDetails;
    private List<LineItemDetailsBean> itemDetailsBeans;

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceVariant() {
        return serviceVariant;
    }

    public void setServiceVariant(String serviceVariant) {
        this.serviceVariant = serviceVariant;
    }

    public String getCpeContractType() {
        return cpeContractType;
    }

    public void setCpeContractType(String cpeContractType) {
        this.cpeContractType = cpeContractType;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public String getTaxExemptionReason() {
        return taxExemptionReason;
    }

    public void setTaxExemptionReason(String taxExemptionReason) {
        this.taxExemptionReason = taxExemptionReason;
    }

    public String getAdditionalIp() {
        return additionalIp;
    }

    public void setAdditionalIp(String additionalIp) {
        this.additionalIp = additionalIp;
    }

    public String getCrossConnectType() {
        return crossConnectType;
    }

    public void setCrossConnectType(String crossConnectType) {
        this.crossConnectType = crossConnectType;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getFiberEntryType() {
        return fiberEntryType;
    }

    public void setFiberEntryType(String fiberEntryType) {
        this.fiberEntryType = fiberEntryType;
    }

    public String getFiberType() {
        return fiberType;
    }

    public void setFiberType(String fiberType) {
        this.fiberType = fiberType;
    }

    public String getFiberPairNumber() {
        return fiberPairNumber;
    }

    public void setFiberPairNumber(String fiberPairNumber) {
        this.fiberPairNumber = fiberPairNumber;
    }

    public String getCablePairNumber() {
        return cablePairNumber;
    }

    public void setCablePairNumber(String cablePairNumber) {
        this.cablePairNumber = cablePairNumber;
    }

    public List<LineItemDetailsBean> getItemDetailsBeans() {
        return itemDetailsBeans;
    }

    public void setItemDetailsBeans(List<LineItemDetailsBean> itemDetailsBeans) {
        this.itemDetailsBeans = itemDetailsBeans;
    }

    public List<AttachmentBean> getAttachmentDetails() {
        return attachmentDetails;
    }

    public void setAttachmentDetails(List<AttachmentBean> attachmentDetails) {
        this.attachmentDetails = attachmentDetails;
    }

    public String getTotalArc() {
        return totalArc;
    }

    public void setTotalArc(String totalArc) {
        this.totalArc = totalArc;
    }

    public String getTotalNrc() {
        return totalNrc;
    }

    public void setTotalNrc(String totalNrc) {
        this.totalNrc = totalNrc;
    }

    public String getTotalMrc() {
        return totalMrc;
    }

    public void setTotalMrc(String totalMrc) {
        this.totalMrc = totalMrc;
    }
}
