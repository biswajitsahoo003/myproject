
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
@JsonPropertyOrder({ "slot-id", "vm-name", "vm-status", "node-type", "host-ip", "cpu-load", "memory-load",
		"load-factor" })
public class NodeStatusList implements Serializable {

	@JsonProperty("slot-id")
	private Integer slotId;
	@JsonProperty("vm-name")
	private String vmName;
	@JsonProperty("vm-status")
	private String vmStatus;
	@JsonProperty("node-type")
	private String nodeType;
	@JsonProperty("host-ip")
	private String hostIp;
	@JsonProperty("cpu-load")
	private String cpuLoad;
	@JsonProperty("memory-load")
	private String memoryLoad;
	@JsonProperty("load-factor")
	private String loadFactor;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	private final static long serialVersionUID = 4652745794639803269L;

	@JsonProperty("slot-id")
	public Integer getSlotId() {
		return slotId;
	}

	@JsonProperty("slot-id")
	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}

	@JsonProperty("vm-name")
	public String getVmName() {
		return vmName;
	}

	@JsonProperty("vm-name")
	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	@JsonProperty("vm-status")
	public String getVmStatus() {
		return vmStatus;
	}

	@JsonProperty("vm-status")
	public void setVmStatus(String vmStatus) {
		this.vmStatus = vmStatus;
	}

	@JsonProperty("node-type")
	public String getNodeType() {
		return nodeType;
	}

	@JsonProperty("node-type")
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	@JsonProperty("host-ip")
	public String getHostIp() {
		return hostIp;
	}

	@JsonProperty("host-ip")
	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	@JsonProperty("cpu-load")
	public String getCpuLoad() {
		return cpuLoad;
	}

	@JsonProperty("cpu-load")
	public void setCpuLoad(String cpuLoad) {
		this.cpuLoad = cpuLoad;
	}

	@JsonProperty("memory-load")
	public String getMemoryLoad() {
		return memoryLoad;
	}

	@JsonProperty("memory-load")
	public void setMemoryLoad(String memoryLoad) {
		this.memoryLoad = memoryLoad;
	}

	@JsonProperty("load-factor")
	public String getLoadFactor() {
		return loadFactor;
	}

	@JsonProperty("load-factor")
	public void setLoadFactor(String loadFactor) {
		this.loadFactor = loadFactor;
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
