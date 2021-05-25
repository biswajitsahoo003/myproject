
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
@JsonPropertyOrder({ "uuid", "name", "status", "cpu-load ", "memory-load", "managementIP", "vncPort", "uptime" })
public class UcpeNodeStatusList implements Serializable {

	@JsonProperty("uuid")
	private String uuid;
	@JsonProperty("name")
	private String name;
	@JsonProperty("status")
	private String status;
	@JsonProperty("cpu-load ")
	private Integer cpuLoad;
	@JsonProperty("memory-load")
	private Integer memoryLoad;
	@JsonProperty("managementIP")
	private String managementIP;
	@JsonProperty("vncPort")
	private Integer vncPort;
	@JsonProperty("uptime")
	private String uptime;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	private final static long serialVersionUID = 4350519154949548855L;

	@JsonProperty("uuid")
	public String getUuid() {
		return uuid;
	}

	@JsonProperty("uuid")
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("cpu-load ")
	public Integer getCpuLoad() {
		return cpuLoad;
	}

	@JsonProperty("cpu-load ")
	public void setCpuLoad(Integer cpuLoad) {
		this.cpuLoad = cpuLoad;
	}

	@JsonProperty("memory-load")
	public Integer getMemoryLoad() {
		return memoryLoad;
	}

	@JsonProperty("memory-load")
	public void setMemoryLoad(Integer memoryLoad) {
		this.memoryLoad = memoryLoad;
	}

	@JsonProperty("managementIP")
	public String getManagementIP() {
		return managementIP;
	}

	@JsonProperty("managementIP")
	public void setManagementIP(String managementIP) {
		this.managementIP = managementIP;
	}

	@JsonProperty("vncPort")
	public Integer getVncPort() {
		return vncPort;
	}

	@JsonProperty("vncPort")
	public void setVncPort(Integer vncPort) {
		this.vncPort = vncPort;
	}

	@JsonProperty("uptime")
	public String getUptime() {
		return uptime;
	}

	@JsonProperty("uptime")
	public void setUptime(String uptime) {
		this.uptime = uptime;
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
