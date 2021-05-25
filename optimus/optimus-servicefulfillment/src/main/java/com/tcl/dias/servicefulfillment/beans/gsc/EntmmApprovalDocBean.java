package com.tcl.dias.servicefulfillment.beans.gsc;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;

public class EntmmApprovalDocBean {
	
	private Integer id;
	
	private AttachmentIdBean document;

	public Integer getId() {
		return id;
	}

	public AttachmentIdBean getDocument() {
		return document;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setDocument(AttachmentIdBean document) {
		this.document = document;
	}
}
