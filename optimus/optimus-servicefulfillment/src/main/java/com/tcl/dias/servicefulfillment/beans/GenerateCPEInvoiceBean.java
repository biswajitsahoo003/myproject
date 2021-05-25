package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This bean has details related to CPE Invoice Generation.
 * 
 * @author arjayapa
 */
public class GenerateCPEInvoiceBean extends TaskDetailsBaseBean {
	
    private String cpeInvoiceNumber;
	
	private List<AttachmentIdBean> documentIds;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String cpeInvoiceDate;

	public String getCpeInvoiceNumber() {
		return cpeInvoiceNumber;
	}

	public void setCpeInvoiceNumber(String cpeInvoiceNumber) {
		this.cpeInvoiceNumber = cpeInvoiceNumber;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getCpeInvoiceDate() {
		return cpeInvoiceDate;
	}

	public void setCpeInvoiceDate(String cpeInvoiceDate) {
		this.cpeInvoiceDate = cpeInvoiceDate;
	}	

}
