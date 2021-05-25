package com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Bean to get app QOS policy response
 * @author archchan
 *
 */
public class AppQosPolicyRules implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("app-qos-policy")
    private List<AppQosPolicy> appQosPolicy = null;

	public List<AppQosPolicy> getAppQosPolicy() {
		return appQosPolicy;
	}

	public void setAppQosPolicy(List<AppQosPolicy> appQosPolicy) {
		this.appQosPolicy = appQosPolicy;
	}

	@Override
	public String toString() {
		return "AppQosPolicyRules [appQosPolicy=" + appQosPolicy + "]";
	}
	
	
	
}
