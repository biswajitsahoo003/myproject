package com.tcl.dias.oms.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class QuoteIllSitesWithFeasiblityAndPricingBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9182414934083319461L;

	private Integer siteId;

	private String siteCode;

	private byte isFeasible;

	private Byte isTaxExempted;

	private List<QuoteIllSitesFeasiblityBean> feasiblityDetails;

	private List<PricingDetailBean> pricingDetails;

	public List<QuoteIllSitesFeasiblityBean> getFeasiblityDetails() {
		return feasiblityDetails;
	}

	public void setFeasiblityDetails(List<QuoteIllSitesFeasiblityBean> feasiblityDetails) {
		this.feasiblityDetails = feasiblityDetails;
	}

	public List<PricingDetailBean> getPricingDetails() {
		return pricingDetails;
	}

	public void setPricingDetails(List<PricingDetailBean> pricingDetails) {
		this.pricingDetails = pricingDetails;
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

	public byte getIsFeasible() {
		return isFeasible;
	}

	public void setIsFeasible(byte isFeasible) {
		this.isFeasible = isFeasible;
	}

	public Byte getIsTaxExempted() {
		return isTaxExempted;
	}

	public void setIsTaxExempted(Byte isTaxExempted) {
		this.isTaxExempted = isTaxExempted;
	}

}
