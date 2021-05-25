package com.tcl.dias.common.beans;

import java.sql.Timestamp;

/**
 * Bean for mapping MFResponseDetail Entity
 *
 * @author ANUSHA UNNI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class MfResponseDetailBean {

	private Integer id;
	private Integer taskId;
	private Integer siteId;
	private String provider;
	private String createResponseJson;
	private String createdBy;
	private String createdTimeStr;
	private Timestamp createdTime;
	private String updatedBy;
	private String updatedTimeStr;
	private Timestamp updatedTime;
	private String type;
	private String feasibilityMode;
	private Integer mfRank;
	private Integer isSelected;
	private String feasibilityStatus;
	private String feasibilityCheck;
	private String feasibilityType;
	private Integer quoteId;
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
	
	public String getOverallSiteStatus() {
		return overallSiteStatus;
	}

	public void setOverallSiteStatus(String overallSiteStatus) {
		this.overallSiteStatus = overallSiteStatus;
	}

	public boolean isHitPrice() {
		return hitPrice;
	}

	public void setHitPrice(boolean hitPrice) {
		this.hitPrice = hitPrice;
	}

	public String getCreatedTimeStr() {
		return createdTimeStr;
	}

	public void setCreatedTimeStr(String createdTimeStr) {
		this.createdTimeStr = createdTimeStr;
	}

	public String getUpdatedTimeStr() {
		return updatedTimeStr;
	}

	public void setUpdatedTimeStr(String updatedTimeStr) {
		this.updatedTimeStr = updatedTimeStr;
	}

	public String getIsUpdated() {
		return isUpdated;
	}

	public void setIsUpdated(String isUpdated) {
		this.isUpdated = isUpdated;
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

	public String getFraId() {
		return fraId;
	}

	public void setFraId(String fraId) {
		this.fraId = fraId;
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

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}
	
}
