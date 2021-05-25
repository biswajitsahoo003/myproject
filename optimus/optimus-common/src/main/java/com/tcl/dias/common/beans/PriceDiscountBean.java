package com.tcl.dias.common.beans;

import java.util.List;

public class PriceDiscountBean {

	private Integer quoteId;
	private String quoteCode;
	private List<SiteDetail> siteDetail;
	private Integer discountApprovalLevel;
	private Integer siteId;
	private String siteCode;
	private String accountName;
	private String quoteCreatedBy;
	private String quoteType;
	private String contractTerm;
	private String optyId;
	private String region;
	private String quoteCreatedUserType;
	
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getQuoteCreatedBy() {
		return quoteCreatedBy;
	}
	public void setQuoteCreatedBy(String quoteCreatedBy) {
		this.quoteCreatedBy = quoteCreatedBy;
	}
	public String getQuoteType() {
		return quoteType;
	}
	public void setQuoteType(String quoteType) {
		this.quoteType = quoteType;
	}
	public String getContractTerm() {
		return contractTerm;
	}
	public void setContractTerm(String contractTerm) {
		this.contractTerm = contractTerm;
	}
	public Integer getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}
	public String getQuoteCode() {
		return quoteCode;
	}
	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}
	
	public Integer getDiscountApprovalLevel() {
		return discountApprovalLevel;
	}
	public void setDiscountApprovalLevel(Integer discountApprovalLevel) {
		this.discountApprovalLevel = discountApprovalLevel;
	}
	public List<SiteDetail> getSiteDetail() {
		return siteDetail;
	}
	public void setSiteDetail(List<SiteDetail> siteDetail) {
		this.siteDetail = siteDetail;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	public String getOptyId() {
		return optyId;
	}
	public void setOptyId(String optyId) {
		this.optyId = optyId;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getQuoteCreatedUserType() {
		return quoteCreatedUserType;
	}
	public void setQuoteCreatedUserType(String quoteCreatedUserType) {
		this.quoteCreatedUserType = quoteCreatedUserType;
	}
	
	
	
	
	
	
}
