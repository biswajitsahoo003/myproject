package com.tcl.dias.servicefulfillment.beans;

import java.io.Serializable;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class JeopardyRequest extends BaseRequest implements Serializable {

	private static final long serialVersionUID = -9158343118036268273L;

	private String action;
	private String reason;
	private String remarks;
	private String groupName;
	
	private String processInitiationType;
	private String orderAmendmentCode;
	private String cancellationOrderCode;
	private String  jeopardyDescription;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getOrderAmendmentCode() {
		return orderAmendmentCode;
	}

	public void setOrderAmendmentCode(String orderAmendmentCode) {
		this.orderAmendmentCode = orderAmendmentCode;
	}

	public String getProcessInitiationType() {
		return processInitiationType;
	}

	public void setProcessInitiationType(String processInitiationType) {
		this.processInitiationType = processInitiationType;
	}

	public String getCancellationOrderCode() {
		return cancellationOrderCode;
	}

	public void setCancellationOrderCode(String cancellationOrderCode) {
		this.cancellationOrderCode = cancellationOrderCode;
	}
	public String getJeopardyDescription() {
		return jeopardyDescription;
	}

	public void setJeopardyDescription(String jeopardyDescription) {
		this.jeopardyDescription = jeopardyDescription;
	}

}