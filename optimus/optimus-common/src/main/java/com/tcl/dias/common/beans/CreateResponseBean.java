package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This file contains the CreateResponseBean.java class.
 * 
 *
 * @author Kruthika S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CreateResponseBean {

	@JsonProperty("task_id")
	private Integer taskId;

	@JsonProperty("site_id")
	private Integer siteId;

	@JsonProperty("create_response_json")
	private String createResponseAttr;

	@JsonProperty("feasibility_mode")
	private String feasibilityMode;
	
	@JsonProperty("feasibility_type")
	private String feasibilityType;
	
	public String getFeasibilityType() {
		return feasibilityType;
	}

	public void setFeasibilityType(String feasibilityType) {
		this.feasibilityType = feasibilityType;
	}

	private String provider;
	private String type;
	private String feasibilityID;
	private String status;
	private String otcOrNrcTotal;
	private String arcTotal;
	private String capexTotal;
	private String oneYrCharges;
    private String createdBy;
	private String updatedBy;
    private String rowId;
    
    // Added for 3D
    private String product;
    
	@JsonProperty("quote_code")
	private String quoteCode;

    public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

    
    public String getQuoteCode() {
		return quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	private String quoteId;

	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getFeasibilityID() {
		return feasibilityID;
	}

	public void setFeasibilityID(String feasibilityID) {
		this.feasibilityID = feasibilityID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOtcOrNrcTotal() {
		return otcOrNrcTotal;
	}

	public void setOtcOrNrcTotal(String otcOrNrcTotal) {
		this.otcOrNrcTotal = otcOrNrcTotal;
	}

	public String getArcTotal() {
		return arcTotal;
	}

	public void setArcTotal(String arcTotal) {
		this.arcTotal = arcTotal;
	}

	public String getCapexTotal() {
		return capexTotal;
	}

	public void setCapexTotal(String capexTotal) {
		this.capexTotal = capexTotal;
	}

	public String getOneYrCharges() {
		return oneYrCharges;
	}

	public void setOneYrCharges(String oneYrCharges) {
		this.oneYrCharges = oneYrCharges;
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
	

	public String getCreateResponseAttr() {
		return createResponseAttr;
	}

	public void setCreateResponseAttr(String createResponseAttr) {
		this.createResponseAttr = createResponseAttr;
	}

}
