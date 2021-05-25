package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class PaymentBean extends TaskDetailsBaseBean {
	
	String rowTransactionId="";
	String prowTransactionId="";

    List<AttachmentIdBean> documentIds;

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }

	public String getRowTransactionId() {
		return rowTransactionId;
	}

	public void setRowTransactionId(String rowTransactionId) {
		this.rowTransactionId = rowTransactionId;
	}

	public String getProwTransactionId() {
		return prowTransactionId;
	}

	public void setProwTransactionId(String prowTransactionId) {
		this.prowTransactionId = prowTransactionId;
	}
    
    
}
