
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_cpe_status;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "ha-configured" })
public class IntraChassisHaStatus implements Serializable {

	@JsonProperty("ha-configured")
	private Boolean haConfigured;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	private final static long serialVersionUID = -3003596001493267511L;

	@JsonProperty("ha-configured")
	public Boolean getHaConfigured() {
		return haConfigured;
	}

	@JsonProperty("ha-configured")
	public void setHaConfigured(Boolean haConfigured) {
		this.haConfigured = haConfigured;
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
