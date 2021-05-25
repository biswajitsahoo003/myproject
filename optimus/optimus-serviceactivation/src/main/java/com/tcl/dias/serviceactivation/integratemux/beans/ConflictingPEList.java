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
@JsonPropertyOrder({ "PEId", "START_DATE_TIME", "END_DATE_TIME" })
public class ConflictingPEList {

	@JsonProperty("PEId")
	private String pEId;
	@JsonProperty("START_DATE_TIME")
	private String sTARTDATETIME;
	@JsonProperty("END_DATE_TIME")
	private String eNDDATETIME;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("PEId")
	public String getPEId() {
		return pEId;
	}

	@JsonProperty("PEId")
	public void setPEId(String pEId) {
		this.pEId = pEId;
	}

	@JsonProperty("START_DATE_TIME")
	public String getSTARTDATETIME() {
		return sTARTDATETIME;
	}

	@JsonProperty("START_DATE_TIME")
	public void setSTARTDATETIME(String sTARTDATETIME) {
		this.sTARTDATETIME = sTARTDATETIME;
	}

	@JsonProperty("END_DATE_TIME")
	public String getENDDATETIME() {
		return eNDDATETIME;
	}

	@JsonProperty("END_DATE_TIME")
	public void setENDDATETIME(String eNDDATETIME) {
		this.eNDDATETIME = eNDDATETIME;
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