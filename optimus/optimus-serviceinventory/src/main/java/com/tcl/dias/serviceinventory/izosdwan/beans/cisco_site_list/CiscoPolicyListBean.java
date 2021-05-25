package com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list;

import java.util.List;

/**
 * Response Bean for policy List API
 * 
 * 
 */
public class CiscoPolicyListBean {

	private String policyType;
	private String policyName;
	private CiscoAssosciatedDefintionBean associatedDefinitionDetails;
	
	
	
	private String policyAlias;
	private String directorRegion;
	public String getPolicyType() {
		return policyType;
	}
	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}
	public String getPolicyName() {
		return policyName;
	}
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}
	public CiscoAssosciatedDefintionBean getAssociatedDefinitionDetails() {
		return associatedDefinitionDetails;
	}
	public void setAssociatedDefinitionDetails(CiscoAssosciatedDefintionBean associatedDefinitionDetails) {
		this.associatedDefinitionDetails = associatedDefinitionDetails;
	}
//	public String getOrganisationName() {
//		return organisationName;
//	}
//	public void setOrganisationName(String organisationName) {
//		this.organisationName = organisationName;
//	}
	public String getPolicyAlias() {
		return policyAlias;
	}
	public void setPolicyAlias(String policyAlias) {
		this.policyAlias = policyAlias;
	}
	public String getDirectorRegion() {
		return directorRegion;
	}
	public void setDirectorRegion(String directorRegion) {
		this.directorRegion = directorRegion;
	}
	
	
	
	@Override
	public String toString() {
		return "CiscoPolicyListBean [policyType=" + policyType + ", policyName=" + policyName
				+ ", associatedDefinitionDetails=" + associatedDefinitionDetails + ", policyAlias=" + policyAlias
				+ ", directorRegion=" + directorRegion + "]";
	}
	
	


	
}
