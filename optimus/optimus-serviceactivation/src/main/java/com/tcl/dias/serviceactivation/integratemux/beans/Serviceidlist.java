package com.tcl.dias.serviceactivation.integratemux.beans;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "serviceId", "conflictChangeIdDetails", "protectionStatus", "primaryImpact",
		"secondaryServiceID" })
public class Serviceidlist {

	@JsonProperty("serviceId")
	private String serviceId;
	@JsonProperty("conflictChangeIdDetails")
	private String conflictChangeIdDetails;
	@JsonProperty("protectionStatus")
	private String protectionStatus;
	@JsonProperty("primaryImpact")
	private String primaryImpact;
	@JsonProperty("secondaryServiceID")
	private String secondaryServiceID;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("serviceId")
	public String getServiceId() {
		return serviceId;
	}

	@JsonProperty("serviceId")
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	@JsonProperty("conflictChangeIdDetails")
	public String getConflictChangeIdDetails() {
		return conflictChangeIdDetails;
	}

	@JsonProperty("conflictChangeIdDetails")
	public void setConflictChangeIdDetails(String conflictChangeIdDetails) {
		this.conflictChangeIdDetails = conflictChangeIdDetails;
	}

	@JsonProperty("protectionStatus")
	public String getProtectionStatus() {
		return protectionStatus;
	}

	@JsonProperty("protectionStatus")
	public void setProtectionStatus(String protectionStatus) {
		this.protectionStatus = protectionStatus;
	}

	@JsonProperty("primaryImpact")
	public String getPrimaryImpact() {
		return primaryImpact;
	}

	@JsonProperty("primaryImpact")
	public void setPrimaryImpact(String primaryImpact) {
		this.primaryImpact = primaryImpact;
	}

	@JsonProperty("secondaryServiceID")
	public String getSecondaryServiceID() {
		return secondaryServiceID;
	}

	@JsonProperty("secondaryServiceID")
	public void setSecondaryServiceID(String secondaryServiceID) {
		this.secondaryServiceID = secondaryServiceID;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}