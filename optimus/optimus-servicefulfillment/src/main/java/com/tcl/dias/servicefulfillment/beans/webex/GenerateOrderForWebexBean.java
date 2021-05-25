package com.tcl.dias.servicefulfillment.beans.webex;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.util.List;

public class GenerateOrderForWebexBean extends TaskDetailsBaseBean {

    private String webexLicenceInvoiceNumber;
    private String webexLicenceIdNo;

    private List<AttachmentIdBean> documentIds;


    public String getWebexLicenceInvoiceNumber() {
        return webexLicenceInvoiceNumber;
    }

    public void setWebexLicenceInvoiceNumber(String webexLicenceInvoiceNumber) {
        this.webexLicenceInvoiceNumber = webexLicenceInvoiceNumber;
    }

    public String getWebexLicenceIdNo() {
		return webexLicenceIdNo;
	}

	public void setWebexLicenceIdNo(String webexLicenceIdNo) {
		this.webexLicenceIdNo = webexLicenceIdNo;
	}

	public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }
}
