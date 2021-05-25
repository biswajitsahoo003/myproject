package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IpcSolutionDetail implements Serializable {

	private static final long serialVersionUID = 5042900815778142832L;
	
	private String offeringName;
	private Double mrc;
	private Double nrc;
	private Double arc;
	private Double ppuRate;
	private List<IpcComponentDetail> components;
	private String cloudCode;
	private String parentCloudCode;
	private Integer assetId;
	private String currency;
	
	public IpcSolutionDetail() {
	}
	public IpcSolutionDetail(String offeringName, Double mrc, Double nrc, Double arc, Double ppuRate,
			String cloudCode, String currency) {
		this.offeringName = offeringName;
		this.mrc = mrc;
		this.nrc = nrc;
		this.arc = arc;
		this.ppuRate = ppuRate;
		this.cloudCode = cloudCode;
		this.currency = currency;
	}
	public String getOfferingName() {
		return offeringName;
	}
	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
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
	public Double getPpuRate() {
		return ppuRate;
	}
	public void setPpuRate(Double ppuRate) {
		this.ppuRate = ppuRate;
	}
	public List<IpcComponentDetail> getComponents() {
		return components;
	}
	public void setComponents(List<IpcComponentDetail> components) {
		this.components = components;
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
	public Integer getAssetId() {
		return assetId;
	}
	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
		
}
