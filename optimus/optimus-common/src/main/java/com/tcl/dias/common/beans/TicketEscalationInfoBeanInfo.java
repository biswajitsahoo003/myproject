package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketEscalationInfoBeanInfo {
	
	private String currentEscalationLevel;
	
	private String allowedForEscalation;
	
	private TicketEscalationWithInfoBean escalationWith;

	public String getCurrentEscalationLevel() {
		return currentEscalationLevel;
	}

	public void setCurrentEscalationLevel(String currentEscalationLevel) {
		this.currentEscalationLevel = currentEscalationLevel;
	}

	public String getAllowedForEscalation() {
		return allowedForEscalation;
	}

	public void setAllowedForEscalation(String allowedForEscalation) {
		this.allowedForEscalation = allowedForEscalation;
	}

	public TicketEscalationWithInfoBean getEscalationWith() {
		return escalationWith;
	}

	public void setEscalationWith(TicketEscalationWithInfoBean escalationWith) {
		this.escalationWith = escalationWith;
	}
	
	
	
	
	

}
