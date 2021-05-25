package com.tcl.dias.oms.crossconnect.pricing.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "customer_account_id",
    "bw_mpbs",
    "active_passive_flag",
    "passive_type",
    "fiber_pairs_count",
    "fiber_entry_flag",
    "fiber_entry_flag_type",
    "site_id",
    "interface",
    "contract_term"
   
})
public class CrossconnectPrcingRequest {
	@JsonProperty("customer_account_id")
    private String customerAccountId;
	@JsonProperty("bw_mpbs")
    private String bwMpbs;
	@JsonProperty("active_passive_flag")
    private String activePassiveFlag;
	@JsonProperty("passive_type")
    private String passiveType;
	@JsonProperty("fiber_pairs_count")
    private String fiberPairsCount;
	@JsonProperty("fiber_entry_flag")
    private Boolean fiberEntryFlag;
	@JsonProperty("fiber_entry_flag_type")
    private String fiberEntryFlagType;
	@JsonProperty("site_id")
    private String siteId;
	@JsonProperty("interface")
    private String Interface;
	@JsonProperty("contract_term")
    private Integer contractTerm;
	
	
	@JsonProperty("contract_term")
	public Integer getContractTerm() {
		return contractTerm;
	}
	@JsonProperty("contract_term")
	public void setContractTerm(Integer contractTerm) {
		this.contractTerm = contractTerm;
	}
	@JsonProperty("interface")
	public String getInterface() {
		return Interface;
	}
	@JsonProperty("interface")
	public void setInterface(String interface1) {
		Interface = interface1;
	}
	@JsonProperty("customer_account_id")
	public String getCustomerAccountId() {
		return customerAccountId;
	}
	@JsonProperty("customer_account_id")
	public void setCustomerAccountId(String customerAccountId) {
		this.customerAccountId = customerAccountId;
	}
	@JsonProperty("bw_mpbs")
	public String getBwMpbs() {
		return bwMpbs;
	}
	@JsonProperty("bw_mpbs")
	public void setBwMpbs(String bwMpbs) {
		this.bwMpbs = bwMpbs;
	}
	@JsonProperty("active_passive_flag")
	public String getActivePassiveFlag() {
		return activePassiveFlag;
	}
	@JsonProperty("active_passive_flag")
	public void setActivePassiveFlag(String activePassiveFlag) {
		this.activePassiveFlag = activePassiveFlag;
	}
	@JsonProperty("passive_type")
	public String getPassiveType() {
		return passiveType;
	}
	@JsonProperty("passive_type")
	public void setPassiveType(String passiveType) {
		this.passiveType = passiveType;
	}
	@JsonProperty("fiber_pairs_count")
	public String getFiberPairsCount() {
		return fiberPairsCount;
	}
	@JsonProperty("fiber_pairs_count")
	public void setFiberPairsCount(String fiberPairsCount) {
		this.fiberPairsCount = fiberPairsCount;
	}
	@JsonProperty("fiber_entry_flag")
	public Boolean getFiberEntryFlag() {
		return fiberEntryFlag;
	}
	@JsonProperty("fiber_entry_flag")
	public void setFiberEntryFlag(Boolean fiberEntryFlag) {
		this.fiberEntryFlag = fiberEntryFlag;
	}
	@JsonProperty("fiber_entry_flag_type")
	public String getFiberEntryFlagType() {
		return fiberEntryFlagType;
	}
	@JsonProperty("fiber_entry_flag_type")
	public void setFiberEntryFlagType(String fiberEntryFlagType) {
		this.fiberEntryFlagType = fiberEntryFlagType;
	}
	@JsonProperty("site_id")
	public String getSiteId() {
		return siteId;
	}
	@JsonProperty("site_id")
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	
	
	
	
}