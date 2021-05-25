package com.tcl.dias.common.beans;

import java.sql.Timestamp;

public class MfNplResponseDetailBean {
	
	private Integer id;
	private Integer taskId;
	private Integer siteId;
	private Integer linkId;
	private String provider;
	private String createResponseJson;
	private String createdBy;
	
	private Timestamp createdTime;
	private String updatedBy;
	
	private Timestamp updatedTime;
	private String type;
	private String feasibilityMode;
	private Integer mfRank;
	private Integer isSelected;
	private String feasibilityStatus;
	private String feasibilityCheck;
	private String feasibilityType;
	private Integer quoteId;
	private String feasibilityModeB;
	private String providerB;
	
	private String fraId;
	private Double otcTotal = 0.0;
	private Double arcTotal = 0.0;
	private Double capexTotal = 0.0;
	private Double totalCharges = 0.0;
	private Double networkCapex = 0.0;
	private String isUpdated;
	private boolean hitPrice;
	private String product;
	private String overallSiteStatus;
	
	private String returnedSiteType;

	public String getReturnedSiteType() {
		return returnedSiteType;
	}

	public void setReturnedSiteType(String returnedSiteType) {
		this.returnedSiteType = returnedSiteType;
	}
	private String feasibilityTypeB;

	public String getFeasibilityTypeB() {
		return feasibilityTypeB;
	}

	public void setFeasibilityTypeB(String feasibilityTypeB) {
		this.feasibilityTypeB = feasibilityTypeB;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public Integer getLinkId() {
		return linkId;
	}
	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getCreateResponseJson() {
		return createResponseJson;
	}
	public void setCreateResponseJson(String createResponseJson) {
		this.createResponseJson = createResponseJson;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Timestamp getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Timestamp getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFeasibilityMode() {
		return feasibilityMode;
	}
	public void setFeasibilityMode(String feasibilityMode) {
		this.feasibilityMode = feasibilityMode;
	}
	public Integer getMfRank() {
		return mfRank;
	}
	public void setMfRank(Integer mfRank) {
		this.mfRank = mfRank;
	}
	public Integer getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(Integer isSelected) {
		this.isSelected = isSelected;
	}
	public String getFeasibilityStatus() {
		return feasibilityStatus;
	}
	public void setFeasibilityStatus(String feasibilityStatus) {
		this.feasibilityStatus = feasibilityStatus;
	}
	public String getFeasibilityCheck() {
		return feasibilityCheck;
	}
	public void setFeasibilityCheck(String feasibilityCheck) {
		this.feasibilityCheck = feasibilityCheck;
	}
	public String getFeasibilityType() {
		return feasibilityType;
	}
	public void setFeasibilityType(String feasibilityType) {
		this.feasibilityType = feasibilityType;
	}
	public Integer getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}
	public String getFeasibilityModeB() {
		return feasibilityModeB;
	}
	public void setFeasibilityModeB(String feasibilityModeB) {
		this.feasibilityModeB = feasibilityModeB;
	}
	public String getProviderB() {
		return providerB;
	}
	public void setProviderB(String providerB) {
		this.providerB = providerB;
	}
	public String getFraId() {
		return fraId;
	}
	public void setFraId(String fraId) {
		this.fraId = fraId;
	}
	public Double getOtcTotal() {
		return otcTotal;
	}
	public void setOtcTotal(Double otcTotal) {
		this.otcTotal = otcTotal;
	}
	public Double getArcTotal() {
		return arcTotal;
	}
	public void setArcTotal(Double arcTotal) {
		this.arcTotal = arcTotal;
	}
	public Double getCapexTotal() {
		return capexTotal;
	}
	public void setCapexTotal(Double capexTotal) {
		this.capexTotal = capexTotal;
	}
	public Double getTotalCharges() {
		return totalCharges;
	}
	public void setTotalCharges(Double totalCharges) {
		this.totalCharges = totalCharges;
	}
	public Double getNetworkCapex() {
		return networkCapex;
	}
	public void setNetworkCapex(Double networkCapex) {
		this.networkCapex = networkCapex;
	}
	public String getIsUpdated() {
		return isUpdated;
	}
	public void setIsUpdated(String isUpdated) {
		this.isUpdated = isUpdated;
	}
	public boolean isHitPrice() {
		return hitPrice;
	}
	public void setHitPrice(boolean hitPrice) {
		this.hitPrice = hitPrice;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getOverallSiteStatus() {
		return overallSiteStatus;
	}
	public void setOverallSiteStatus(String overallSiteStatus) {
		this.overallSiteStatus = overallSiteStatus;
	}
	
	

}
