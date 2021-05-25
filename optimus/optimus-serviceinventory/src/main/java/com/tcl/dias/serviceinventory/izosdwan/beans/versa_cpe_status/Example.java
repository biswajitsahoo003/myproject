
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
@JsonPropertyOrder({ "versanms.ApplianceStatusResult" })
public class Example implements Serializable {

	@JsonProperty("versanms.ApplianceStatusResult")
	private VersanmsApplianceStatusResult versanmsApplianceStatusResult;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	private final static long serialVersionUID = 8042354865995288029L;

	@JsonProperty("versanms.ApplianceStatusResult")
	public VersanmsApplianceStatusResult getVersanmsApplianceStatusResult() {
		return versanmsApplianceStatusResult;
	}

	@JsonProperty("versanms.ApplianceStatusResult")
	public void setVersanmsApplianceStatusResult(VersanmsApplianceStatusResult versanmsApplianceStatusResult) {
		this.versanmsApplianceStatusResult = versanmsApplianceStatusResult;
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
