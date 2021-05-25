package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class SalesNegotiationBean extends BaseRequest {

	private String processInitiationType;

	private String orderAmendmentCode;

	private String cancellationOrderCode;

	public String getProcessInitiationType() {
		return processInitiationType;
	}

	public void setProcessInitiationType(String processInitiationType) {
		this.processInitiationType = processInitiationType;
	}

	public String getOrderAmendmentCode() {
		return orderAmendmentCode;
	}

	public void setOrderAmendmentCode(String orderAmendmentCode) {
		this.orderAmendmentCode = orderAmendmentCode;
	}

	public String getCancellationOrderCode() {
		return cancellationOrderCode;
	}

	public void setCancellationOrderCode(String cancellationOrderCode) {
		this.cancellationOrderCode = cancellationOrderCode;
	}

}
