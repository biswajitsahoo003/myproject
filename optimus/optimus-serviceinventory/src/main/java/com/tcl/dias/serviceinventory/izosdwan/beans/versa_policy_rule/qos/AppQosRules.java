package com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;


public class AppQosRules implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("rules")
    private AppQosPolicyRules appQosPolicyRules = null;

	@JsonProperty("rules")
	public AppQosPolicyRules getAppQosPolicy() {
		return appQosPolicyRules;
	}

	@JsonProperty("rules")
	public void setAppQosPolicy(AppQosPolicyRules appQosPolicyRules) {
		this.appQosPolicyRules = appQosPolicyRules;
	}

	@Override
	public String toString() {
		return "AppQosPolicyRule [rules=" + appQosPolicyRules + "]";
	}
}
