package com.tcl.dias.oms.npl.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PricingInputBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("Account_id_with_18_Digit")
	private String accountId18Digit;

	@JsonProperty("Last_Modified_Date")
	private String lastModifiedDate;

	@JsonProperty("opportunityTerm")
	private String opportunityTerm;

	@JsonProperty("Site_id")
	private String siteId;

	@JsonProperty("quoteType_quote")
	private String quoteTypeQuote;

	@JsonProperty("BW_mbps_upd")
	private String BWMbpsUpd;

	@JsonProperty("connection_type")
	private String connectionType;

	@JsonProperty("sum_no_of_sites_uni_len")
	private String sumNoOfSitesUniLen;

	@JsonProperty("Sum_Onnet_Flag")
	private String sumOnnetFlag;

	@JsonProperty("Sum_Offnet_Flag")
	private String sumOffnetFlag;

	@JsonProperty("CPE_Variant")
	private String cpeVariant;

	@JsonProperty("CPE_management_type")
	private String cpeManagementType;

	@JsonProperty("CPE_supply_type")
	private String cpeSupplyType;

	@JsonProperty("Last_Mile_Cost_NRC")
	private String lastMileCostNRC;

	@JsonProperty("Last_Mile_Cost_ARC")
	private String lastMileCostARC;

	@JsonProperty("topology")
	private String topology;

	@JsonProperty("pool_size")
	private String poolSize;

	@JsonProperty("additional_IP")
	private String additionalIP;

	@JsonProperty("Burstable_BW")
	private String burstableBW;
	
	// NPL related attributes - start
	@JsonProperty("DistanceBetweenPOPs")
	private String distanceBetweenPOPs;
	
	@JsonProperty("sum_model_name")
	private String sumModelName;
	
	@JsonProperty("model_name_transform")
	private String modelNameTransform;

	@JsonProperty("product_flavor_transform")
	private String productFlavorTransform;

	@JsonProperty("DistanceBetweenPOPs")
	public String getDistanceBetweenPOPs() {
		return distanceBetweenPOPs;
	}

	@JsonProperty("DistanceBetweenPOPs")
	public void setDistanceBetweenPOPs(String distanceBetweenPOPs) {
		this.distanceBetweenPOPs = distanceBetweenPOPs;
	}
	
	@JsonProperty("sum_model_name")
	public String getSumModelName() {
		return sumModelName;
	}

	@JsonProperty("sum_model_name")
	public void setSumModelName(String sumModelName) {
		this.sumModelName = sumModelName;
	}

	@JsonProperty("model_name_transform")
	public String getModelNameTransform() {
		return modelNameTransform;
	}

	@JsonProperty("model_name_transform")
	public void setModelNameTransform(String modelNameTransform) {
		this.modelNameTransform = modelNameTransform;
	}

	@JsonProperty("product_flavor_transform")
	public String getProductFlavorTransform() {
		return productFlavorTransform;
	}

	@JsonProperty("product_flavor_transform")
	public void setProductFlavorTransform(String productFlavorTransform) {
		this.productFlavorTransform = productFlavorTransform;
	}
	
	// NPL related attributes - end


	@JsonProperty("Account_id_with_18_Digit")
	public String getAccountId18Digit() {
		return accountId18Digit;
	}

	@JsonProperty("Account_id_with_18_Digit")
	public void setAccountId18Digit(String accountId18Digit) {
		this.accountId18Digit = accountId18Digit;
	}

	@JsonProperty("Last_Modified_Date")
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	@JsonProperty("Last_Modified_Date")
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@JsonProperty("opportunityTerm")
	public String getOpportunityTerm() {
		return opportunityTerm;
	}

	@JsonProperty("opportunityTerm")
	public void setOpportunityTerm(String opportunityTerm) {
		this.opportunityTerm = opportunityTerm;
	}

	@JsonProperty("Site_id")
	public String getSiteId() {
		return siteId;
	}

	@JsonProperty("Site_id")
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	@JsonProperty("quoteType_quote")
	public String getQuoteTypeQuote() {
		return quoteTypeQuote;
	}

	@JsonProperty("quoteType_quote")
	public void setQuoteTypeQuote(String quoteTypeQuote) {
		this.quoteTypeQuote = quoteTypeQuote;
	}

	@JsonProperty("BW_mbps_upd")
	public String getBWMbpsUpd() {
		return BWMbpsUpd;
	}

	@JsonProperty("BW_mbps_upd")
	public void setBWMbpsUpd(String bWMbpsUpd) {
		BWMbpsUpd = bWMbpsUpd;
	}

	@JsonProperty("connection_type")
	public String getConnectionType() {
		return connectionType;
	}

	@JsonProperty("connection_type")
	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	@JsonProperty("sum_no_of_sites_uni_len")
	public String getSumNoOfSitesUniLen() {
		return sumNoOfSitesUniLen;
	}

	@JsonProperty("sum_no_of_sites_uni_len")
	public void setSumNoOfSitesUniLen(String sumNoOfSitesUniLen) {
		this.sumNoOfSitesUniLen = sumNoOfSitesUniLen;
	}

	@JsonProperty("Sum_Onnet_Flag")
	public String getSumOnnetFlag() {
		return sumOnnetFlag;
	}

	@JsonProperty("Sum_Onnet_Flag")
	public void setSumOnnetFlag(String sumOnnetFlag) {
		this.sumOnnetFlag = sumOnnetFlag;
	}

	@JsonProperty("Sum_Offnet_Flag")
	public String getSumOffnetFlag() {
		return sumOffnetFlag;
	}

	@JsonProperty("Sum_Offnet_Flag")
	public void setSumOffnetFlag(String sumOffnetFlag) {
		this.sumOffnetFlag = sumOffnetFlag;
	}

	@JsonProperty("CPE_Variant")
	public String getCpeVariant() {
		return cpeVariant;
	}

	@JsonProperty("CPE_Variant")
	public void setCpeVariant(String cpeVariant) {
		this.cpeVariant = cpeVariant;
	}

	@JsonProperty("CPE_management_type")
	public String getCpeManagementType() {
		return cpeManagementType;
	}

	@JsonProperty("CPE_management_type")
	public void setCpeManagementType(String cpeManagementType) {
		this.cpeManagementType = cpeManagementType;
	}

	@JsonProperty("CPE_supply_type")
	public String getCpeSupplyType() {
		return cpeSupplyType;
	}

	@JsonProperty("CPE_supply_type")
	public void setCpeSupplyType(String cpeSupplyType) {
		this.cpeSupplyType = cpeSupplyType;
	}

	@JsonProperty("Last_Mile_Cost_NRC")
	public String getLastMileCostNRC() {
		return lastMileCostNRC;
	}

	@JsonProperty("Last_Mile_Cost_NRC")
	public void setLastMileCostNRC(String lastMileCostNRC) {
		this.lastMileCostNRC = lastMileCostNRC;
	}

	@JsonProperty("Last_Mile_Cost_ARC")
	public String getLastMileCostARC() {
		return lastMileCostARC;
	}

	@JsonProperty("Last_Mile_Cost_ARC")
	public void setLastMileCostARC(String lastMileCostARC) {
		this.lastMileCostARC = lastMileCostARC;
	}

	@JsonProperty("topology")
	public String getTopology() {
		return topology;
	}

	@JsonProperty("topology")
	public void setTopology(String topology) {
		this.topology = topology;
	}

	@JsonProperty("pool_size")
	public String getPoolSize() {
		return poolSize;
	}

	@JsonProperty("pool_size")
	public void setPoolSize(String poolSize) {
		this.poolSize = poolSize;
	}

	@JsonProperty("additional_IP")
	public String getAdditionalIP() {
		return additionalIP;
	}

	@JsonProperty("additional_IP")
	public void setAdditionalIP(String additionalIP) {
		this.additionalIP = additionalIP;
	}

	@JsonProperty("Burstable_BW")
	public String getBurstableBW() {
		return burstableBW;
	}

	@JsonProperty("Burstable_BW")
	public void setBurstableBW(String burstableBW) {
		this.burstableBW = burstableBW;
	}

}
