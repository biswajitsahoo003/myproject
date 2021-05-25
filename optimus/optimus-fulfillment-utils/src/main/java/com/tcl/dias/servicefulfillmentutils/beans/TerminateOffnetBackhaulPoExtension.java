package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class TerminateOffnetBackhaulPoExtension extends BaseRequest {

	private String extensionApproved;
	
	private String offnetTerminationDate;
	private String isSupplierEtcAvailable;
	private String supplierEtcCharges;
	private List<AttachmentIdBean> documentIds;

	public String getOffnetTerminationDate() {
		return offnetTerminationDate;
	}

	public void setOffnetTerminationDate(String offnetTerminationDate) {
		this.offnetTerminationDate = offnetTerminationDate;
	}

	public String getIsSupplierEtcAvailable() {
		return isSupplierEtcAvailable;
	}

	public void setIsSupplierEtcAvailable(String isSupplierEtcAvailable) {
		this.isSupplierEtcAvailable = isSupplierEtcAvailable;
	}

	public String getSupplierEtcCharges() {
		return supplierEtcCharges;
	}

	public void setSupplierEtcCharges(String supplierEtcCharges) {
		this.supplierEtcCharges = supplierEtcCharges;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getExtensionApproved() {
		return extensionApproved;
	}

	public void setExtensionApproved(String extensionApproved) {
		this.extensionApproved = extensionApproved;
	}

}
