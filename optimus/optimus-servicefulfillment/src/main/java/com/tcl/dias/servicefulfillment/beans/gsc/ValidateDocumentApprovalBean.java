package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DIDSupplierBean;

public class ValidateDocumentApprovalBean {

	private Integer id;
	private String status;
	private String remarks;
	private AttachmentIdBean document;

	@JsonIgnoreProperties(ignoreUnknown = true)
	private List<DIDSupplierBean> selectedSuppliers;

	public List<DIDSupplierBean> getSelectedSuppliers() {
		return selectedSuppliers;
	}

	public void setSelectedSuppliers(List<DIDSupplierBean> selectedSuppliers) {
		this.selectedSuppliers = selectedSuppliers;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public AttachmentIdBean getDocument() {
		return document;
	}

	public void setDocument(AttachmentIdBean document) {
		this.document = document;
	}

	@Override
	public String toString() {
		return "DocumentRemarks [id=" + id + ", status=" + status + ", remarks=" + remarks + "]";
	}

}
