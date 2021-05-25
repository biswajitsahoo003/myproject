package com.tcl.dias.servicefulfillment.beans;

import java.io.Serializable;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class RaiseDependencyBean extends BaseRequest implements Serializable {

	private static final long serialVersionUID = -9158343118036268273L;

	private String action;
	private String reason;
	private String remarks;

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

	@Override
	public String toString() {
		return "RaiseDependencyBean{" +
				"action='" + action + '\'' +
				", reason='" + reason + '\'' +
				", remarks='" + remarks + '\'' +
				'}';
	}
}