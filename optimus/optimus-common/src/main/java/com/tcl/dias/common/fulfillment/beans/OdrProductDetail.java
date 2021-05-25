package com.tcl.dias.common.fulfillment.beans;

import java.sql.Timestamp;
import java.util.List;

/**
 * 
 * This file contains the bean for holding the IPC order product details values.
 * 
 *
 * @author Mohamed Danish A
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class OdrProductDetail {

	private Integer id;

	private String type;

	private String solutionName;

	private Double mrc;

	private Double nrc;

	private Double arc;

	private String isActive;

	private String createdBy;

	private Timestamp createdDate;

	private String updatedBy;

	private Timestamp updatedDate;
	
	private String cloudCode;

	private String parentCloudCode;

	private List<OdrProductDetailAttributes> odrProductAttributes;

	private List<OdrServiceCommercial> odrServiceCommercials;

	private Double ppuRate;

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

	public List<OdrProductDetailAttributes> getOdrProductAttributes() {
		return odrProductAttributes;
	}

	public void setOdrProductAttributes(List<OdrProductDetailAttributes> odrProductAttributes) {
		this.odrProductAttributes = odrProductAttributes;
	}

	public List<OdrServiceCommercial> getOdrServiceCommercials() {
		return odrServiceCommercials;
	}

	public void setOdrServiceCommercials(List<OdrServiceCommercial> odrServiceCommercials) {
		this.odrServiceCommercials = odrServiceCommercials;
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

	public Double getPpuRate() {
		return ppuRate;
	}

	public void setPpuRate(Double ppuRate) {
		this.ppuRate = ppuRate;
	}

}
