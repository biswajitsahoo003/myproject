package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class ConfirmMuxRecovery extends BaseRequest {

	private List<AttachmentIdBean> documentIds;
	private String isMuxRecovered;
	private String muxSerialNumber;

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getIsMuxRecovered() {
		return isMuxRecovered;
	}

	public void setIsMuxRecovered(String isMuxRecovered) {
		this.isMuxRecovered = isMuxRecovered;
	}

	public String getMuxSerialNumber() {
		return muxSerialNumber;
	}

	public void setMuxSerialNumber(String muxSerialNumber) {
		this.muxSerialNumber = muxSerialNumber;
	}

}
