package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

public class LLDPreparationBean extends TaskDetailsBaseBean{

    private String scheduledDate;
    private String scheduledFromTime;
    private String scheduledToTime;
    private String customerContactName;
    private String customerContactMobile;
    private String customerContactEmailId;
    private String ipDetailsReceived;
    private String sddSignedOff;
    private String customerPOReceived;

    private List<AttachmentIdBean> documentIds;
    private String momDetails;

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getScheduledFromTime() {
        return scheduledFromTime;
    }

    public void setScheduledFromTime(String scheduledFromTime) {
        this.scheduledFromTime = scheduledFromTime;
    }

    public String getScheduledToTime() {
        return scheduledToTime;
    }

    public void setScheduledToTime(String scheduledToTime) {
        this.scheduledToTime = scheduledToTime;
    }

    public String getCustomerContactName() {
        return customerContactName;
    }

    public void setCustomerContactName(String customerContactName) {
        this.customerContactName = customerContactName;
    }

    public String getCustomerContactMobile() {
        return customerContactMobile;
    }

    public void setCustomerContactMobile(String customerContactMobile) {
        this.customerContactMobile = customerContactMobile;
    }

    public String getCustomerContactEmailId() {
        return customerContactEmailId;
    }

    public void setCustomerContactEmailId(String customerContactEmailId) {
        this.customerContactEmailId = customerContactEmailId;
    }

    public String getCustomerPOReceived() {
        return customerPOReceived;
    }

    public void setCustomerPOReceived(String customerPOReceived) {
        this.customerPOReceived = customerPOReceived;
    }

    public String getIpDetailsReceived() {
        return ipDetailsReceived;
    }

    public void setIpDetailsReceived(String ipDetailsReceived) {
        this.ipDetailsReceived = ipDetailsReceived;
    }

    public String getSddSignedOff() {
        return sddSignedOff;
    }

    public void setSddSignedOff(String sddSignedOff) {
        this.sddSignedOff = sddSignedOff;
    }

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }

    public String getMomDetails() {
        return momDetails;
    }

    public void setMomDetails(String momDetails) {
        this.momDetails = momDetails;
    }



}
