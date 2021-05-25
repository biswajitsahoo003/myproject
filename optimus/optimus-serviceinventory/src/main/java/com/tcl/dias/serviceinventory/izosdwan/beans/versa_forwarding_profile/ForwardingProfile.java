package com.tcl.dias.serviceinventory.izosdwan.beans.versa_forwarding_profile;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "forwarding-profile" })
public class ForwardingProfile {

	@JsonProperty("forwarding-profile")
	private ForwardingProfile_ forwardingProfile;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	private final static long serialVersionUID = 9076281856459071576L;

	@JsonProperty("forwarding-profile")
	public ForwardingProfile_ getForwardingProfile() {
		return forwardingProfile;
	}

	@JsonProperty("forwarding-profile")
	public void setForwardingProfile(ForwardingProfile_ forwardingProfile) {
		this.forwardingProfile = forwardingProfile;
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
