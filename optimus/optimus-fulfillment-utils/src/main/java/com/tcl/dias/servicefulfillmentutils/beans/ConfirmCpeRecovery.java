package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class ConfirmCpeRecovery extends BaseRequest {

	private List<AttachmentIdBean> documentIds;
	private String isCpeRecovered;
	private String distributionCenterName;
	private String distributionCenterAddress;

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getIsCpeRecovered() {
		return isCpeRecovered;
	}

	public void setIsCpeRecovered(String isCpeRecovered) {
		this.isCpeRecovered = isCpeRecovered;
	}

	public String getDistributionCenterName() {
		return distributionCenterName;
	}

	public void setDistributionCenterName(String distributionCenterName) {
		this.distributionCenterName = distributionCenterName;
	}

	public String getDistributionCenterAddress() {
		return distributionCenterAddress;
	}

	public void setDistributionCenterAddress(String distributionCenterAddress) {
		this.distributionCenterAddress = distributionCenterAddress;
	}
	
	

}
