package com.tcl.dias.oms.beans;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class QuoteIllSitesFeasiblityBean implements Serializable {
	private Integer id;

	private Integer siteId;

	private String feasibilityCheck;

	private String feasibilityCode;

	private String feasibilityMode;

	private Integer rank;

	private String provider;

	private String response;

	private Byte isSelected;

	private String type;

	private String feasibilityType;

	private String sfdcFeasibilityId;

	private Timestamp createdTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getFeasibilityCheck() {
		return feasibilityCheck;
	}

	public void setFeasibilityCheck(String feasibilityCheck) {
		this.feasibilityCheck = feasibilityCheck;
	}

	public String getFeasibilityMode() {
		return feasibilityMode;
	}

	public void setFeasibilityMode(String feasibilityMode) {
		this.feasibilityMode = feasibilityMode;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Byte getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Byte isSelected) {
		this.isSelected = isSelected;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getFeasibilityCode() {
		return feasibilityCode;
	}

	public void setFeasibilityCode(String feasibilityCode) {
		this.feasibilityCode = feasibilityCode;
	}

	public String getSfdcFeasibilityId() {
		return sfdcFeasibilityId;
	}

	public void setSfdcFeasibilityId(String sfdcFeasibilityId) {
		this.sfdcFeasibilityId = sfdcFeasibilityId;
	}

	public String getFeasibilityType() {
		return feasibilityType;
	}

	public void setFeasibilityType(String feasibilityType) {
		this.feasibilityType = feasibilityType;
	}

}
