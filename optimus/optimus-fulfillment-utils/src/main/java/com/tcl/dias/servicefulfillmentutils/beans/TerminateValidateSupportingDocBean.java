package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

import java.util.List;

public class TerminateValidateSupportingDocBean extends BaseRequest {

    private String reason;
    private String terminationDocRemarks;
    private List<AttachmentIdBean> documentIds;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTerminationDocRemarks() {
        return terminationDocRemarks;
    }

    public void setTerminationDocRemarks(String terminationDocRemarks) {
        this.terminationDocRemarks = terminationDocRemarks;
    }

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }
}
