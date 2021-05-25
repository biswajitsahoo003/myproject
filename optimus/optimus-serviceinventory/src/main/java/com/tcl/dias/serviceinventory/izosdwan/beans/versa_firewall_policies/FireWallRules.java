package com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_policies;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;


/** 
 * This class used for fire wall policy rearrangement policy rules response
 * @author - Sobhan Ganta
 * */
public class FireWallRules implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("rules")
    private AccessFireWallPolicyRules accessFireWallPolicyRules = null;
	
	
	@JsonProperty("rules")
	public AccessFireWallPolicyRules getAccessPolicy() {
		return accessFireWallPolicyRules;
	}
	
	public void setAccessPolicy(AccessFireWallPolicyRules accessFireWallPolicyRules) {
		this.accessFireWallPolicyRules = accessFireWallPolicyRules;
	}

	@Override
	public String toString() {
		return "FireWallRules [accessFireWallPolicyRules=" + accessFireWallPolicyRules + "]";
	}
	
}
