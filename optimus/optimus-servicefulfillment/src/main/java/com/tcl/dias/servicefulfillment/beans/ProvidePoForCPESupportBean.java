package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This bean is used to provide details related to CPE Support.
 * 
 * @author arjayapa
 */
public class ProvidePoForCPESupportBean extends TaskDetailsBaseBean {
	
	private String cpeSupportPoNumber;
	
	private String cpeSupportVendorName;
	
	private List<AttachmentIdBean> documentIds;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String cpeSupportPoDate;

	public String getCpeSupportPoNumber() {
		return cpeSupportPoNumber;
	}

	public void setCpeSupportPoNumber(String cpeSupportPoNumber) {
		this.cpeSupportPoNumber = cpeSupportPoNumber;
	}

	public String getCpeSupportVendorName() {
		return cpeSupportVendorName;
	}

	public void setCpeSupportVendorName(String cpeSupportVendorName) {
		this.cpeSupportVendorName = cpeSupportVendorName;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getCpeSupportPoDate() {
		return cpeSupportPoDate;
	}

	public void setCpeSupportPoDate(String cpeSupportPoDate) {
		this.cpeSupportPoDate = cpeSupportPoDate;
	}
}
