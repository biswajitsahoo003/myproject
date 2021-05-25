package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

public class SelectSupplierBean {
    private String emailTrigger;
    private String emailFrom;
    private String emailCc;
    private String manualOrderStatus;
    private String referenceNumber;
    private Integer sequenceNo;
    private List<Integer> attachmentId;
    private List<RoutingDetailBean> routingDetails;

    public String getEmailTrigger() {
        return emailTrigger;
    }

    public void setEmailTrigger(String emailTrigger) {
        this.emailTrigger = emailTrigger;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailCc() {
        return emailCc;
    }

    public void setEmailCc(String emailCc) {
        this.emailCc = emailCc;
    }

    public String getManualOrderStatus() {
        return manualOrderStatus;
    }

    public void setManualOrderStatus(String manualOrderStatus) {
        this.manualOrderStatus = manualOrderStatus;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public List<RoutingDetailBean> getRoutingDetails() {
        return routingDetails;
    }

    public void setRoutingDetails(List<RoutingDetailBean> routingDetails) {
        this.routingDetails = routingDetails;
    }

    public List<Integer> getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(List<Integer> attachmentId) {
        this.attachmentId = attachmentId;
    }
}
