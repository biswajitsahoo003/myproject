package com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppQosRule {
	private static final long serialVersionUID = 1L;
	@JsonProperty("app-qos-policy")
	private AppQosPolicy appQosPolicy = null;

	@JsonProperty("app-qos-policy")
	public AppQosPolicy getAppQosPolicy() {
		return appQosPolicy;
	}

	@JsonProperty("app-qos-policy")
	public void setAppQosPolicy(AppQosPolicy appQosPolicy) {
		this.appQosPolicy = appQosPolicy;
	}

	@Override
	public String toString() {
		return "AppQosPolicyRule [appQosPolicy=" + appQosPolicy + "]";
	}
}
