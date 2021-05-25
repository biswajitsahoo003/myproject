package com.tcl.dias.servicefulfillment.beans.webex;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class GenerateWebexEndpointInvoiceBean extends TaskDetailsBaseBean {

	private String webexEndpointInvoiceNumber;
	
	private List<AttachmentIdBean> documentIds;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String webexEndpointInvoiceDate;

	public String getWebexEndpointInvoiceNumber() {
		return webexEndpointInvoiceNumber;
	}

	public void setWebexEndpointInvoiceNumber(String webexEndpointInvoiceNumber) {
		this.webexEndpointInvoiceNumber = webexEndpointInvoiceNumber;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getWebexEndpointInvoiceDate() {
		return webexEndpointInvoiceDate;
	}

	public void setWebexEndpointInvoiceDate(String webexEndpointInvoiceDate) {
		this.webexEndpointInvoiceDate = webexEndpointInvoiceDate;
	}
}
