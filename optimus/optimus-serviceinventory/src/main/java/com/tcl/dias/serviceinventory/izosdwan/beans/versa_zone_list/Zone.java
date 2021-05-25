
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_zone_list;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name", "networks", "routing-instance", "interface-list" })
public class Zone {

	@JsonProperty("name")
	private String name;
	@JsonProperty("networks")
	private List<String> networks = null;
	@JsonProperty("routing-instance")
	private String routingInstance;
	@JsonProperty("interface-list")
	private List<String> interfaceList = null;

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("networks")
	public List<String> getNetworks() {
		return networks;
	}

	@JsonProperty("networks")
	public void setNetworks(List<String> networks) {
		this.networks = networks;
	}

	@JsonProperty("routing-instance")
	public String getRoutingInstance() {
		return routingInstance;
	}

	@JsonProperty("routing-instance")
	public void setRoutingInstance(String routingInstance) {
		this.routingInstance = routingInstance;
	}

	@JsonProperty("interface-list")
	public List<String> getInterfaceList() {
		return interfaceList;
	}

	@JsonProperty("interface-list")
	public void setInterfaceList(List<String> interfaceList) {
		this.interfaceList = interfaceList;
	}

}
