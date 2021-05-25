package com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Bean class to fetch App QOS policy details for given policy name
 * @author archchan
 *
 */
public class AppQosPolicyRule implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("app-qos-policy")
    private AppQosPolicy appQosPolicy = null;

	@JsonProperty("rules")
	public AppQosPolicy getAppQosPolicy() {
		return appQosPolicy;
	}

	@JsonProperty("rules")
	public void setAppQosPolicy(AppQosPolicy appQosPolicy) {
		this.appQosPolicy = appQosPolicy;
	}

	@Override
	public String toString() {
		return "AppQosPolicyRule [rules=" + appQosPolicy + "]";
	}
}
