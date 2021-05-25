package com.tcl.dias.oms.gsc.beans;

import java.util.List;

import com.tcl.dias.oms.gvpn.pdf.beans.GvpnQuotePdfBean;

/**
 * Quote data bean to accommodate gsc multi LE scenario
 * 
 * @author Srinivasa Raghavan
 * 
 */
public class GscMultiLEQuoteDataBean {

	private Integer quoteId;
	private String quoteCode;
	private Integer customerId;
	private String productFamilyName;
	private String accessType;
	private String profileName;
	private Integer quoteVersion;
	private String engagementOptyId;
	private List<GscMultipleLESolutionBean> solutionsToBeAdded;
	private List<GscMultiQuoteLeBean> quoteToLes;
	private String stage;
	private String subStage;

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

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getProductFamilyName() {
		return productFamilyName;
	}

	public void setProductFamilyName(String productFamilyName) {
		this.productFamilyName = productFamilyName;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public Integer getQuoteVersion() {
		return quoteVersion;
	}

	public void setQuoteVersion(Integer quoteVersion) {
		this.quoteVersion = quoteVersion;
	}

	public List<GscMultiQuoteLeBean> getQuoteToLes() {
		return quoteToLes;
	}

	public void setQuoteToLes(List<GscMultiQuoteLeBean> quoteToLes) {
		this.quoteToLes = quoteToLes;
	}

	public List<GscMultipleLESolutionBean> getSolutionsToBeAdded() {
		return solutionsToBeAdded;
	}

	public void setSolutionsToBeAdded(List<GscMultipleLESolutionBean> solutionsToBeAdded) {
		this.solutionsToBeAdded = solutionsToBeAdded;
	}

	public String getEngagementOptyId() {
		return engagementOptyId;
	}

	public void setEngagementOptyId(String engagementOptyId) {
		this.engagementOptyId = engagementOptyId;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getSubStage() {
		return subStage;
	}

	public void setSubStage(String subStage) {
		this.subStage = subStage;
	}
}
