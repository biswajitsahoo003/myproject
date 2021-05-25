package com.tcl.dias.common.servicefulfillment.beans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This file contains the bean for holding the Sc order product details values.
 * 
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class ScProductDetailBean {

	private Integer id;

	private String type;

	private String solutionName;

	private Double mrc;

	private Double nrc;

	private Double arc;
	
	private Double ppuRate;

	private String isActive;

	private String createdBy;

	private Timestamp createdDate;

	private String updatedBy;

	private Timestamp updatedDate;
	
	private String cloudCode;

	private String parentCloudCode;
	
	private String businessUnit;
	
	private String zone;
	
	private String environment;

	private List<ScProductDetailAttributesBean> scProductAttributes = new ArrayList<>();

	private List<ScServiceCommercialBean> scServiceCommercials = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public List<ScProductDetailAttributesBean> getScProductAttributes() {
		return scProductAttributes;
	}

	public void setScProductAttributes(List<ScProductDetailAttributesBean> scProductAttributes) {
		this.scProductAttributes = scProductAttributes;
	}

	public List<ScServiceCommercialBean> getScServiceCommercials() {
		return scServiceCommercials;
	}

	public void setScServiceCommercials(List<ScServiceCommercialBean> scServiceCommercials) {
		this.scServiceCommercials = scServiceCommercials;
	}

	public String getCloudCode() {
		return cloudCode;
	}

	public void setCloudCode(String cloudCode) {
		this.cloudCode = cloudCode;
	}

	public String getParentCloudCode() {
		return parentCloudCode;
	}

	public void setParentCloudCode(String parentCloudCode) {
		this.parentCloudCode = parentCloudCode;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public Double getPpuRate() {
		return ppuRate;
	}

	public void setPpuRate(Double ppuRate) {
		this.ppuRate = ppuRate;
	}
}
