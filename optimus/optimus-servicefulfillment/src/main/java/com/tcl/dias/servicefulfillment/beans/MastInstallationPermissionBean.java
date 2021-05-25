/**
 * 
 */
package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.DocumentIds;

import java.util.List;

/**
 * for Mast Installation Permisssion API
 * @author yomagesh
 *
 */
public class MastInstallationPermissionBean extends BaseRequest {



	private String delayReason;

	private String rejectReason;

	private List<String> requiredDocuments;

	private String otherDocument;

	private List<AttachmentIdBean> documentIds;

	public List<AttachmentIdBean> getDocumentIds() { return documentIds; }

	public void setDocumentIds(List<AttachmentIdBean> documentIds) { this.documentIds = documentIds; }

	public String getDelayReason() {
		return delayReason;
	}
	public void setDelayReason(String delayReason) {
		this.delayReason = delayReason;
	}
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public List<String> getRequiredDocuments() { return requiredDocuments; }

	public void setRequiredDocuments(List<String> requiredDocuments) {
		this.requiredDocuments = requiredDocuments;
	}

	public String getOtherDocument() {
		return otherDocument;
	}

	public void setOtherDocument(String otherDocument) {
		this.otherDocument = otherDocument;
	}
}
