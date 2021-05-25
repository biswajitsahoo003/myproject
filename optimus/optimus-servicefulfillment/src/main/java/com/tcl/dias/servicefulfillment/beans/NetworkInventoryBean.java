package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import com.tcl.dias.common.beans.AccessRingInfo;
import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;

public class NetworkInventoryBean extends BaseRequest{

	private List<AccessRingInfo> accessRingInfoList;
	
	private String type;
	
	private String selectedRing;

	private List<AttachmentIdBean> documentIds;

	private String networkSuggestionRemarks;

	

	
	

	public String getSelectedRing() {
		return selectedRing;
	}

	public void setSelectedRing(String selectedRing) {
		this.selectedRing = selectedRing;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<AccessRingInfo> getAccessRingInfoList() {
		return accessRingInfoList;
	}

	public void setAccessRingInfoList(List<AccessRingInfo> accessRingInfoList) {
		this.accessRingInfoList = accessRingInfoList;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getNetworkSuggestionRemarks() { return networkSuggestionRemarks; }

	public void setNetworkSuggestionRemarks(String networkSuggestionRemarks) { this.networkSuggestionRemarks = networkSuggestionRemarks; }
}
