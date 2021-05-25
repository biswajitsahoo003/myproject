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
@JsonPropertyOrder({ "SANSAstatus", "conflictingPEstatus" })
public class StatusSummary {

	@JsonProperty("SANSAstatus")
	private String sANSAstatus;
	@JsonProperty("conflictingPEstatus")
	private String conflictingPEstatus;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("SANSAstatus")
	public String getSANSAstatus() {
		return sANSAstatus;
	}

	@JsonProperty("SANSAstatus")
	public void setSANSAstatus(String sANSAstatus) {
		this.sANSAstatus = sANSAstatus;
	}

	@JsonProperty("conflictingPEstatus")
	public String getConflictingPEstatus() {
		return conflictingPEstatus;
	}

	@JsonProperty("conflictingPEstatus")
	public void setConflictingPEstatus(String conflictingPEstatus) {
		this.conflictingPEstatus = conflictingPEstatus;
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