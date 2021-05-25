package com.tcl.dias.servicefulfillment.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class CreateMrnBean extends TaskDetailsBaseBean {

    private String mrnNumber;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String mrnDate;
    private String requestedBy;
    private String verifiedBy;
    private String approvedBy;
    private List<AttachmentIdBean> documentIds;
    private String distributionCenterName;
    private String distributionCenterAddress;
    private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDistributionCenterName() {
        return distributionCenterName;
    }

    public void setDistributionCenterName(String distributionCenterName) {
        this.distributionCenterName = distributionCenterName;
    }

    public String getDistributionCenterAddress() {
        return distributionCenterAddress;
    }

    public void setDistributionCenterAddress(String distributionCenterAddress) {
        this.distributionCenterAddress = distributionCenterAddress;
    }

    public String getMrnNumber() {
        return mrnNumber;
    }

    public void setMrnNumber(String mrnNumber) {
        this.mrnNumber = mrnNumber;
    }

    public String getMrnDate() {
        return mrnDate;
    }

    public void setMrnDate(String mrnDate) {
        this.mrnDate = mrnDate;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }
}
