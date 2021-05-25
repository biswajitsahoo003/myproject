package com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans;

import java.util.List;

/**
 * Response bean for sending Cpes insync and outsync list
 * 
 * @author Srinivasa Raghavan
 */
public class CiscoPolicyStatusResponse {
	private List<String> applicationListId;
	private String definitionId;
	private String updateStatus;
	private String policyName;
	private String policyType;

	public List<String> getApplicationListId() {
		return applicationListId;
	}
	public void setApplicationListId(List<String> applicationListId) {
		this.applicationListId = applicationListId;
	}
	public String getUpdateStatus() {
		return updateStatus;
	}
	public void setUpdateStatus(String updateStatus) {
		this.updateStatus = updateStatus;
	}
	public String getPolicyName() {
		return policyName;
	}
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}
	public String getPolicyType() {
		return policyType;
	}
	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}
	public String getDefinitionId() {
		return definitionId;
	}
	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

}
