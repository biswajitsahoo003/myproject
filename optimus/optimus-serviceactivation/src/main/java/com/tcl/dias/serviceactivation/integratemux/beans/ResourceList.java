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
@JsonPropertyOrder({ "resourceName", "resourceType" })
public class ResourceList {

	@JsonProperty("resourceName")
	private String resourceName;
	@JsonProperty("resourceType")
	private String resourceType;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("resourceName")
	public String getResourceName() {
		return resourceName;
	}

	@JsonProperty("resourceName")
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	@JsonProperty("resourceType")
	public String getResourceType() {
		return resourceType;
	}

	@JsonProperty("resourceType")
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
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