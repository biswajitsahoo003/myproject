package com.tcl.dias.common.fulfillment.beans;

import java.util.List;

public class TerminationDropRequest {

	private String opOrderCode;
	private String reason;

	List<String> serviceCodes;

	public String getOpOrderCode() {
		return opOrderCode;
	}

	public void setOpOrderCode(String opOrderCode) {
		this.opOrderCode = opOrderCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public List<String> getServiceCodes() {
		return serviceCodes;
	}

	public void setServiceCodes(List<String> serviceCodes) {
		this.serviceCodes = serviceCodes;
	}

}
