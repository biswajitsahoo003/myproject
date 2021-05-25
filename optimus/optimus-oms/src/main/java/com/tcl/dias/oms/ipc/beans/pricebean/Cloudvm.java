
package com.tcl.dias.oms.ipc.beans.pricebean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "itemId",
    "type",
    "region",
    "pricingModel",
    "count",
    "perGBAdditionalIOPSForSSD",
    "variant",
    "vcpu",
    "vram",
    "rootStorage",
    "additionalStorages",
    "os",
    "hypervisor",
    "nrc",
    "mrc",
    "ppuRate",
    "term",
    "managementEnabled", 
    "ipcCommonComponentId"
})
public class Cloudvm {

    @JsonProperty("itemId")
    private String itemId;
    @JsonProperty("type")
    private String type;
    @JsonProperty("region")
    private String region;
    @JsonProperty("pricingModel")
    private String pricingModel;
    @JsonProperty("count")
    private Integer count;
    @JsonProperty("perGBAdditionalIOPSForSSD")
    private String perGBAdditionalIOPSForSSD;
    @JsonProperty("variant")
    private String variant;
    @JsonProperty("vcpu")
    private String vcpu;
    @JsonProperty("vram")
    private String vram;
    @JsonProperty("rootStorage")
    private RootStorage rootStorage;
    @JsonProperty("additionalStorages")
    private List<AdditionalStorage> additionalStorages = new ArrayList<>();
    @JsonProperty("os")
    private String os;
    @JsonProperty("hypervisor")
    private String hypervisor;
    @JsonProperty("nrc")
    private Double nrc;
    @JsonProperty("mrc")
    private Double mrc;
    @JsonProperty("ppuRate")
    private Double ppuRate;
    @JsonProperty("term")
    private Integer term;
    @JsonProperty("managementEnabled")
    private Boolean managementEnabled;
    @JsonProperty("ipcCommonComponentId")
    private Integer ipcCommonComponentId;

    @JsonProperty("itemId")
    public String getItemId() {
        return itemId;
    }

    @JsonProperty("itemId")
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("region")
    public String getRegion() {
        return region;
    }

    @JsonProperty("region")
    public void setRegion(String region) {
        this.region = region;
    }
    
    @JsonProperty("pricingModel")
    public String getPricingModel() {
		return pricingModel;
	}

    @JsonProperty("pricingModel")
	public void setPricingModel(String pricingModel) {
		this.pricingModel = pricingModel;
	}

    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonProperty("perGBAdditionalIOPSForSSD")
    public String getPerGBAdditionalIOPSForSSD() {
        return perGBAdditionalIOPSForSSD;
    }

    @JsonProperty("perGBAdditionalIOPSForSSD")
    public void setPerGBAdditionalIOPSForSSD(String perGBAdditionalIOPSForSSD) {
        this.perGBAdditionalIOPSForSSD = perGBAdditionalIOPSForSSD;
    }

    @JsonProperty("variant")
    public String getVariant() {
        return variant;
    }

    @JsonProperty("variant")
    public void setVariant(String variant) {
        this.variant = variant;
    }

    @JsonProperty("vcpu")
    public String getVcpu() {
        return vcpu;
    }

    @JsonProperty("vcpu")
    public void setVcpu(String vcpu) {
        this.vcpu = vcpu;
    }

    @JsonProperty("vram")
    public String getVram() {
        return vram;
    }

    @JsonProperty("vram")
    public void setVram(String vram) {
        this.vram = vram;
    }

    @JsonProperty("rootStorage")
    public RootStorage getRootStorage() {
        return rootStorage;
    }

    @JsonProperty("rootStorage")
    public void setRootStorage(RootStorage rootStorage) {
        this.rootStorage = rootStorage;
    }

    @JsonProperty("additionalStorages")
    public List<AdditionalStorage> getAdditionalStorages() {
        return additionalStorages;
    }

    @JsonProperty("additionalStorages")
    public void setAdditionalStorages(List<AdditionalStorage> additionalStorages) {
        this.additionalStorages = additionalStorages;
    }

    @JsonProperty("os")
    public String getOs() {
        return os;
    }

    @JsonProperty("os")
    public void setOs(String os) {
        this.os = os;
    }

    @JsonProperty("hypervisor")
    public String getHypervisor() {
        return hypervisor;
    }

    @JsonProperty("hypervisor")
    public void setHypervisor(String hypervisor) {
        this.hypervisor = hypervisor;
    }

    @JsonProperty("nrc")
    public Double getNrc() {
        return nrc;
    }

    @JsonProperty("nrc")
    public void setNrc(Double nrc) {
        this.nrc = nrc;
    }

    @JsonProperty("mrc")
    public Double getMrc() {
        return mrc;
    }

    @JsonProperty("mrc")
    public void setMrc(Double mrc) {
        this.mrc = mrc;
    }
    
    @JsonProperty("ppuRate")
	public Double getPpuRate() {
		return ppuRate;
	}

    @JsonProperty("ppuRate")
	public void setPpuRate(Double ppuRate) {
		this.ppuRate = ppuRate;
	}

    @JsonProperty("term")
    public Integer getTerm() {
        return term;
    }

    @JsonProperty("term")
    public void setTerm(Integer term) {
        this.term = term;
    }

    @JsonProperty("managementEnabled")
    public Boolean getManagementEnabled() {
        return managementEnabled;
    }

    @JsonProperty("managementEnabled")
    public void setManagementEnabled(Boolean managementEnabled) {
        this.managementEnabled = managementEnabled;
    }

    @JsonProperty("ipcCommonComponentId")
    public Integer getIpcCommonComponentId() {
		return ipcCommonComponentId;
	}

    @JsonProperty("ipcCommonComponentId")
	public void setIpcCommonComponentId(Integer ipcCommonComponentId) {
		this.ipcCommonComponentId = ipcCommonComponentId;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this).append("itemId", itemId).append("type", type).append("region", region).append("pricingModel", pricingModel).append("count", count).append("perGBAdditionalIOPSForSSD", perGBAdditionalIOPSForSSD).append("variant", variant).append("vcpu", vcpu).append("vram", vram).append("rootStorage", rootStorage).append("additionalStorages", additionalStorages).append("os", os).append("hypervisor", hypervisor).append("nrc", nrc).append("mrc", mrc).append("ppuRate", ppuRate).append("term", term).append("managementEnabled", managementEnabled).toString();
    }

}
