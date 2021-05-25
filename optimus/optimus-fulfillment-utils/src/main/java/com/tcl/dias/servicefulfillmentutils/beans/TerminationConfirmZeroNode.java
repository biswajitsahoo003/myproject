package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class TerminationConfirmZeroNode extends BaseRequest {

	private String zeroNodeOrNot;
	private String eorDetails;
	private List<AttachmentIdBean> documentIds;
	
	

	/**
	 * @return the documentIds
	 */
	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	/**
	 * @param documentIds the documentIds to set
	 */
	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getZeroNodeOrNot() {
		return zeroNodeOrNot;
	}

	public void setZeroNodeOrNot(String zeroNodeOrNot) {
		this.zeroNodeOrNot = zeroNodeOrNot;
	}

	public String getEorDetails() {
		return eorDetails;
	}

	public void setEorDetails(String eorDetails) {
		this.eorDetails = eorDetails;
	}

}
