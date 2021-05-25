package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;
import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class ServiceAcceptanceBean extends BaseRequest {

    private String issueType;
    private Boolean serviceAccepted;
    private String issueDescription;
    private List<AttachmentIdBean> documentIds;
    private List<UnderlayDetails> underlayDetails;

    public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}
	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public Boolean getServiceAccepted() {
        return serviceAccepted;
    }

    public void setServiceAccepted(Boolean serviceAccepted) {
        this.serviceAccepted = serviceAccepted;
    }

	public String getIssueDescription() {
		return issueDescription;
	}

	public void setIssueDescription(String issueDescription) {
		this.issueDescription = issueDescription;
	}

    public List<UnderlayDetails> getUnderlayDetails() {
        return underlayDetails;
    }
    public void setUnderlayDetails(List<UnderlayDetails> underlayDetails) {
        this.underlayDetails = underlayDetails;
    }
}
