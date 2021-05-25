package com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_policies;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.AccessPolicy;

/**
 * Bean to get firewall Access Policy response
 * 
 * @author sobhan Ganta
 *
 */
public class AccessFireWallPolicyRules implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("access-policy")
	private List<AccessPolicy> accessPolicy = null;

	public List<AccessPolicy> getAccessPolicy() {
		return accessPolicy;
	}

	public void setAccessPolicy(List<AccessPolicy> accessPolicy) {
		this.accessPolicy = accessPolicy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "AccessFireWallPolicyRules [accessPolicy=" + accessPolicy + "]";
	}

}
