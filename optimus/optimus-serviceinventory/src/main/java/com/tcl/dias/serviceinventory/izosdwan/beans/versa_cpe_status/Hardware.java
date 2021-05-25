
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_cpe_status;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "model", "cpuCores", "memory", "freeMemory", "diskSize", "freeDisk", "lpm", "fanless",
		"intelQuickAssistAcceleration","firmwareVersion", "manufacturer", "serialNo","hardWareSerialNo", "cpuModel","cpuCount","cpuLoad", "interfaceCount", "ssd",
		"packageName", "sku" })

public class Hardware {

	@JsonProperty("model")
	private String model;
	@JsonProperty("cpuCores")
	private String cpuCores;
	@JsonProperty("memory")
	private String memory;
	@JsonProperty("freeMemory")
	private String freeMemory;
	@JsonProperty("diskSize")
	private String diskSize;
	@JsonProperty("freeDisk")
	private String freeDisk;
	@JsonProperty("lpm")
	private String lpm;
	@JsonProperty("fanless")
	private String fanless;
	@JsonProperty("intelQuickAssistAcceleration")
	private String intelQuickAssistAcceleration;
	@JsonProperty("firmwareVersion")
	private String firmwareVersion;
	@JsonProperty("manufacturer")
	private String manufacturer;
	@JsonProperty("serialNo")
	private String serialNo;
	@JsonProperty("hardWareSerialNo")
	private String hardWareSerialNo;
	@JsonProperty("cpuModel")
	private String cpuModel;
	@JsonProperty("cpuCount")
	private String cpuCount;
	@JsonProperty("cpuLoad")
	private String cpuLoad;
	@JsonProperty("interfaceCount")
	private String interfaceCount;
	@JsonProperty("ssd")
	private String isSSD;
	@JsonProperty("packageName")
	private String packageName;
	@JsonProperty("sku")
	private String sku;

	@JsonProperty("model")
	public String getModel() {
		return model;
	}

	@JsonProperty("model")
	public void setModel(String model) {
		this.model = model;
	}

	@JsonProperty("cpuCores")
	public String getCpuCores() {
		return cpuCores;
	}

	@JsonProperty("cpuCores")
	public void setCpuCores(String cpuCores) {
		this.cpuCores = cpuCores;
	}

	@JsonProperty("memory")
	public String getMemory() {
		return memory;
	}

	@JsonProperty("memory")
	public void setMemory(String memory) {
		this.memory = memory;
	}

	@JsonProperty("freeMemory")
	public String getFreeMemory() {
		return freeMemory;
	}

	@JsonProperty("freeMemory")
	public void setFreeMemory(String freeMemory) {
		this.freeMemory = freeMemory;
	}

	@JsonProperty("diskSize")
	public String getDiskSize() {
		return diskSize;
	}

	@JsonProperty("diskSize")
	public void setDiskSize(String diskSize) {
		this.diskSize = diskSize;
	}

	@JsonProperty("freeDisk")
	public String getFreeDisk() {
		return freeDisk;
	}

	@JsonProperty("freeDisk")
	public void setFreeDisk(String freeDisk) {
		this.freeDisk = freeDisk;
	}

	@JsonProperty("lpm")
	public String getLpm() {
		return lpm;
	}

	@JsonProperty("lpm")
	public void setLpm(String lpm) {
		this.lpm = lpm;
	}

	@JsonProperty("fanless")
	public String getFanless() {
		return fanless;
	}

	@JsonProperty("fanless")
	public void setFanless(String fanless) {
		this.fanless = fanless;
	}

	@JsonProperty("intelQuickAssistAcceleration")
	public String getIntelQuickAssistAcceleration() {
		return intelQuickAssistAcceleration;
	}

	@JsonProperty("intelQuickAssistAcceleration")
	public void setIntelQuickAssistAcceleration(String intelQuickAssistAcceleration) {
		this.intelQuickAssistAcceleration = intelQuickAssistAcceleration;
	}

	@JsonProperty("manufacturer")
	public String getManufacturer() {
		return manufacturer;
	}

	@JsonProperty("manufacturer")
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	@JsonProperty("serialNo")
	public String getSerialNo() {
		return serialNo;
	}

	@JsonProperty("serialNo")
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	@JsonProperty("cpuModel")
	public String getCpuModel() {
		return cpuModel;
	}

	@JsonProperty("cpuModel")
	public void setCpuModel(String cpuModel) {
		this.cpuModel = cpuModel;
	}

	@JsonProperty("cpuCount")
	public String getCpuCount() {
		return cpuCount;
	}

	@JsonProperty("cpuCount")
	public void setCpuCount(String cpuCount) {
		this.cpuCount = cpuCount;
	}

	@JsonProperty("interfaceCount")
	public String getInterfaceCount() {
		return interfaceCount;
	}

	@JsonProperty("interfaceCount")
	public void setInterfaceCount(String interfaceCount) {
		this.interfaceCount = interfaceCount;
	}

	@JsonProperty("ssd")
	public String getIsSSD() {
		return isSSD;
	}

	@JsonProperty("ssd")
	public void setIsSSD(String isSSD) {
		this.isSSD = isSSD;
	}

	@JsonProperty("packageName")
	public String getPackageName() {
		return packageName;
	}

	@JsonProperty("packageName")
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@JsonProperty("sku")
	public String getSku() {
		return sku;
	}

	@JsonProperty("sku")
	public void setSku(String sku) {
		this.sku = sku;
	}
	@JsonProperty("firmwareVersion")
	public String getFirmwareVersion() {
		return firmwareVersion;
	}
	@JsonProperty("firmwareVersion")
	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}
	@JsonProperty("cpuLoad")	
	public String getCpuLoad() {
		return cpuLoad;
	}
	@JsonProperty("cpuLoad")
	public void setCpuLoad(String cpuLoad) {
		this.cpuLoad = cpuLoad;
	}
	
	

}
