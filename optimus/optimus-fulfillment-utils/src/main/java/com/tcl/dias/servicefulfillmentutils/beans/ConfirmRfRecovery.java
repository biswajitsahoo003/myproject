package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class ConfirmRfRecovery extends BaseRequest {

	private List<AttachmentIdBean> documentIds;
	private String isRfRecovered;
	private String isLanExtentionUsed;
	
	
	public String getIsLanExtentionUsed() {
		return isLanExtentionUsed;
	}

	public void setIsLanExtentionUsed(String isLanExtentionUsed) {
		this.isLanExtentionUsed = isLanExtentionUsed;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getIsRfRecovered() {
		return isRfRecovered;
	}

	public void setIsRfRecovered(String isRfRecovered) {
		this.isRfRecovered = isRfRecovered;
	}

}
