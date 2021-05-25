package com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list;

import java.util.List;

/**
 * Bean for PolicyTypeList
 * 
 * @author 
 */
public class PolicyTypeList {
	private String policyType;
	private List<String> policyName;

	public List<String> getPolicyName() {
		return policyName;
	}

	public void setPolicyName(List<String> policyName) {
		this.policyName = policyName;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}
	
	
	
	
}