package com.tcl.dias.common.beans;

import java.sql.Timestamp;



public class FetchResponseBean {
	
	private Integer id;
	
	private Integer taskId;

	private Integer siteId ;
	
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
	
	private int otcTotal;
	
	private int arcTotal;
	
	private int capexTotal;
	
	private int totalCharges;
	
	private int networkCapex;
	

	public int getNetworkCapex() {
		return networkCapex;
	}

	public void setNetworkCapex(int networkCapex) {
		this.networkCapex = networkCapex;
	}

	public int getOtcTotal() {
		return otcTotal;
	}

	public void setOtcTotal(int otcTotal) {
		this.otcTotal = otcTotal;
	}

	public int getArcTotal() {
		return arcTotal;
	}

	public void setArcTotal(int arcTotal) {
		this.arcTotal = arcTotal;
	}

	public int getCapexTotal() {
		return capexTotal;
	}

	public void setCapexTotal(int capexTotal) {
		this.capexTotal = capexTotal;
	}

	public int getTotalCharges() {
		return totalCharges;
	}

	public void setTotalCharges(int totalCharges) {
		this.totalCharges = totalCharges;
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
}
