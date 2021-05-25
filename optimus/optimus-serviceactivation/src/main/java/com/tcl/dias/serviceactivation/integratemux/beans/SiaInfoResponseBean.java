package com.tcl.dias.serviceactivation.integratemux.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "requestid", "resourceList", "StatusSummary", "ConflictingPEList", "serviceidlist" })
public class SiaInfoResponseBean {

	@JsonProperty("requestid")
	private String requestid;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("resourceList")
	private List<ResourceList> resourceList = null;
	@JsonProperty("StatusSummary")
	private StatusSummary statusSummary;
	@JsonProperty("ConflictingPEList")
	private List<ConflictingPEList> conflictingPEList = null;
	@JsonProperty("serviceidlist")
	private List<Serviceidlist> serviceidlist = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("requestid")
	public String getRequestid() {
		return requestid;
	}

	@JsonProperty("requestid")
	public void setRequestid(String requestid) {
		this.requestid = requestid;
	}

	@JsonProperty("resourceList")
	public List<ResourceList> getResourceList() {
		return resourceList;
	}

	@JsonProperty("resourceList")
	public void setResourceList(List<ResourceList> resourceList) {
		this.resourceList = resourceList;
	}

	@JsonProperty("StatusSummary")
	public StatusSummary getStatusSummary() {
		return statusSummary;
	}

	@JsonProperty("StatusSummary")
	public void setStatusSummary(StatusSummary statusSummary) {
		this.statusSummary = statusSummary;
	}

	@JsonProperty("ConflictingPEList")
	public List<ConflictingPEList> getConflictingPEList() {
		return conflictingPEList;
	}

	@JsonProperty("ConflictingPEList")
	public void setConflictingPEList(List<ConflictingPEList> conflictingPEList) {
		this.conflictingPEList = conflictingPEList;
	}

	@JsonProperty("serviceidlist")
	public List<Serviceidlist> getServiceidlist() {
		return serviceidlist;
	}

	@JsonProperty("serviceidlist")
	public void setServiceidlist(List<Serviceidlist> serviceidlist) {
		this.serviceidlist = serviceidlist;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public String getStatus() {
		return status;
	}

	@JsonProperty("Status")
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("Status")
	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
}