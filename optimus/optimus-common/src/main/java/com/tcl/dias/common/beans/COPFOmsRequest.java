package com.tcl.dias.common.beans;

import java.util.List;
/**
 * 
 * This file contains the COPFOmsRequest bean class
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class COPFOmsRequest {
	private String systemGeneratingCopfIdC;
	private String currencyIsoCode;
	private String recordTypeName;
	private String opportunityId;
	private String productServiceId;
	private Integer tpsId;
	private List<LinkCOPFDetails> linkCOPFDetails;
	
	public List<LinkCOPFDetails> getLinkCOPFDetails() {
		return linkCOPFDetails;
	}
	public void setLinkCOPFDetails(List<LinkCOPFDetails> linkCOPFDetails) {
		this.linkCOPFDetails = linkCOPFDetails;
	}
	public String getSystemGeneratingCopfIdC() {
		return systemGeneratingCopfIdC;
	}
	public void setSystemGeneratingCopfIdC(String systemGeneratingCopfIdC) {
		this.systemGeneratingCopfIdC = systemGeneratingCopfIdC;
	}
	public String getCurrencyIsoCode() {
		return currencyIsoCode;
	}
	public void setCurrencyIsoCode(String currencyIsoCode) {
		this.currencyIsoCode = currencyIsoCode;
	}
	public String getRecordTypeName() {
		return recordTypeName;
	}
	public void setRecordTypeName(String recordTypeName) {
		this.recordTypeName = recordTypeName;
	}
	public String getOpportunityId() {
		return opportunityId;
	}
	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}
	public String getProductServiceId() {
		return productServiceId;
	}
	public void setProductServiceId(String productServiceId) {
		this.productServiceId = productServiceId;
	}
	public Integer getTpsId() {
		return tpsId;
	}
	public void setTpsId(Integer tpsId) {
		this.tpsId = tpsId;
	}
	
	

}
