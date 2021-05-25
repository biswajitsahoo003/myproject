package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class ConfirmDeferredDeliveryBean extends BaseRequest {

	private String tentativeDateForRejectService;
	private String remarks;

	public String getTentativeDateForRejectService() {
		return tentativeDateForRejectService;
	}

	public void setTentativeDateForRejectService(String tentativeDateForRejectService) {
		this.tentativeDateForRejectService = tentativeDateForRejectService;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
