
package com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Versa APIs stubs (CPE status)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "data" })
public class CiscoPolicyActivatedDetail implements Serializable {
	@JsonProperty("policyVersion")
	private String policyVersion;
	@JsonProperty("lastUpdatedBy")
	private String lastUpdatedBy;
	@JsonProperty("policyName")
	private String activatedPolicyName;
	@JsonProperty("lastUpdatedOn")
	private String lastUpdatedOn;
	@JsonProperty("policyType")
	private String policyType;
	@JsonProperty("createdBy")
	private String createdBy;
	@JsonProperty("policyId")
	private String policyId;
	@JsonProperty("@rid")
	private String rid;
	@JsonProperty("policyDescription")
	private String policyDescription;
	@JsonProperty("isPolicyActivated")
	private boolean isPolicyActivated;
	@JsonProperty("createdOn")
	private String createdOn;
	@JsonProperty("policyDefinition")
	private String policyDefinition;
	private Assembly assembly;
	public String getPolicyVersion() {
		return policyVersion;
	}
	public void setPolicyVersion(String policyVersion) {
		this.policyVersion = policyVersion;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getActivatedPolicyName() {
		return activatedPolicyName;
	}
	public void setActivatedPolicyName(String activatedPolicyName) {
		this.activatedPolicyName = activatedPolicyName;
	}
	public String getLastUpdatedOn() {
		return lastUpdatedOn;
	}
	public void setLastUpdatedOn(String lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}
	public String getPolicyType() {
		return policyType;
	}
	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getPolicyId() {
		return policyId;
	}
	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public String getPolicyDescription() {
		return policyDescription;
	}
	public void setPolicyDescription(String policyDescription) {
		this.policyDescription = policyDescription;
	}
	
	public boolean isPolicyActivated() {
		return isPolicyActivated;
	}
	public void setPolicyActivated(boolean isPolicyActivated) {
		this.isPolicyActivated = isPolicyActivated;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	
	
	
	public String getPolicyDefinition() {
		return policyDefinition;
	}
	public void setPolicyDefinition(String policyDefinition) {
		this.policyDefinition = policyDefinition;
	}
	
	public Assembly getAssembly() {
		return assembly;
	}
	public void setAssembly(Assembly assembly) {
		this.assembly = assembly;
	}
	@Override
	public String toString() {
		return "CiscoPolicyActivatedDetail [policyVersion=" + policyVersion + ", lastUpdatedBy=" + lastUpdatedBy
				+ ", activatedPolicyName=" + activatedPolicyName + ", lastUpdatedOn=" + lastUpdatedOn + ", policyType="
				+ policyType + ", createdBy=" + createdBy + ", policyId=" + policyId + ", rid=" + rid
				+ ", policyDescription=" + policyDescription + ", isPolicyActivated=" + isPolicyActivated
				+ ", createdOn=" + createdOn + ", policyDefinition=" + policyDefinition + "]";
	}
	

}
