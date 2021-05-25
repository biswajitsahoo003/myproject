
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
@JsonPropertyOrder({ "nodeStatusList" })
public class Nodes implements Serializable {

	@JsonProperty("nodeStatusList")
	private NodeStatusList nodeStatusList;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	private final static long serialVersionUID = 3285540764190064831L;

	@JsonProperty("nodeStatusList")
	public NodeStatusList getNodeStatusList() {
		return nodeStatusList;
	}

	@JsonProperty("nodeStatusList")
	public void setNodeStatusList(NodeStatusList nodeStatusList) {
		this.nodeStatusList = nodeStatusList;
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
