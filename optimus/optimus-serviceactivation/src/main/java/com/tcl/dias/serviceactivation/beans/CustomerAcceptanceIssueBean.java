package com.tcl.dias.serviceactivation.beans;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import com.tcl.dias.servicefulfillmentutils.beans.UnderlayBean;

import java.io.Serializable;
import java.util.List;

public class CustomerAcceptanceIssueBean extends TaskDetailsBaseBean {


    private String remarks;
    private String commissioningDate;
    private List<AttachmentIdBean> documentIds;
    private List<UnderlayBean> underlayBeans;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCommissioningDate() {
        return commissioningDate;
    }

    public void setCommissioningDate(String commissioningDate) {
        this.commissioningDate = commissioningDate;
    }

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }

    public List<UnderlayBean> getUnderlayBeans() {
        return underlayBeans;
    }

    public void setUnderlayBeans(List<UnderlayBean> underlayBeans) {
        this.underlayBeans = underlayBeans;
    }
}
