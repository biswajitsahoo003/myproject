package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This bean is used to provide details related to CPE Installation.
 * 
 * @author arjayapa
 */
public class ProvidePoForCPEInstallationBean extends TaskDetailsBaseBean {
	
	private String cpeInstallationPoNumber;
	
	private String cpeInstallationVendorName;
	
	private List<AttachmentIdBean> documentIds;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String cpeInstallationPoDate;
	
	
	private String cpeSupportPoNumber;
	
	private String cpeSupportVendorName;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String cpeSupportPoDate;

	public String getCpeInstallationPoNumber() {
		return cpeInstallationPoNumber;
	}

	public void setCpeInstallationPoNumber(String cpeInstallationPoNumber) {
		this.cpeInstallationPoNumber = cpeInstallationPoNumber;
	}

	public String getCpeInstallationVendorName() {
		return cpeInstallationVendorName;
	}

	public void setCpeInstallationVendorName(String cpeInstallationVendorName) {
		this.cpeInstallationVendorName = cpeInstallationVendorName;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getCpeInstallationPoDate() {
		return cpeInstallationPoDate;
	}

	public void setCpeInstallationPoDate(String cpeInstallationPoDate) {
		this.cpeInstallationPoDate = cpeInstallationPoDate;
	}

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

	public String getCpeSupportPoDate() {
		return cpeSupportPoDate;
	}

	public void setCpeSupportPoDate(String cpeSupportPoDate) {
		this.cpeSupportPoDate = cpeSupportPoDate;
	}	
	
	

}
