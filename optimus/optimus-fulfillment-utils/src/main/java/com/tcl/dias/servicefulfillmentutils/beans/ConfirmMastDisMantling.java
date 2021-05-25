package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class ConfirmMastDisMantling extends BaseRequest {

	private List<AttachmentIdBean> documentIds;
	private String isMastDismantled;
	private String ownerShipTransfer;

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getIsMastDismantled() {
		return isMastDismantled;
	}

	public void setIsMastDismantled(String isMastDismantled) {
		this.isMastDismantled = isMastDismantled;
	}

	public String getOwnerShipTransfer() {
		return ownerShipTransfer;
	}

	public void setOwnerShipTransfer(String ownerShipTransfer) {
		this.ownerShipTransfer = ownerShipTransfer;
	}
	
	
	

}
