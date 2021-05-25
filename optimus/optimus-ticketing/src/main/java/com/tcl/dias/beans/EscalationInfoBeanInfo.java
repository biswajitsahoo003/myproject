package com.tcl.dias.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EscalationInfoBeanInfo {
	
	private String currentEscalationLevel;
	
	private String allowedForEscalation;
	
	private EscalationWithInfoBean escalationWith;

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

	public EscalationWithInfoBean getEscalationWith() {
		return escalationWith;
	}

	public void setEscalationWith(EscalationWithInfoBean escalationWith) {
		this.escalationWith = escalationWith;
	}
	
	
	
	
	

}
