package com.tcl.dias.oms.gde.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GdeCreateScheduleInputs request bean 
 * @author archchan
 *
 */
public class GdeCreateScheduleInputs {
	
	@JsonProperty("feasibility_check_id")
	private String feasibilityCheckId;
	
	@JsonProperty("callback_url")
	private String callbackUrl;

	public String getFeasibilityCheckId() {
		return feasibilityCheckId;
	}

	public void setFeasibilityCheckId(String feasibilityCheckId) {
		this.feasibilityCheckId = feasibilityCheckId;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	
	

}
