package com.tcl.dias.oms.npl.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.beans.PricingDetailBean;

@JsonInclude(Include.NON_NULL)
public class NplPricingFeasibilityBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9182414934083319461L;

	private Integer linkId;

	private String linkCode;

	private Byte isFeasible;
	
    private Byte isTaxExemptedSiteA;
	
	private Byte isTaxExemptedSiteB;

	private List<NplFeasiblityBean> feasiblityDetails;

	private List<PricingDetailBean> pricingDetails;
	
	private String chargeableDistance;

	public String getChargeableDistance() {
		return chargeableDistance;
	}

	public void setChargeableDistance(String chargeableDistance) {
		this.chargeableDistance = chargeableDistance;
	}

	public Byte getIsTaxExemptedSiteA() {
		return isTaxExemptedSiteA;
	}

	public void setIsTaxExemptedSiteA(Byte isTaxExemptedSiteA) {
		this.isTaxExemptedSiteA = isTaxExemptedSiteA;
	}

	public Byte getIsTaxExemptedSiteB() {
		return isTaxExemptedSiteB;
	}

	public void setIsTaxExemptedSiteB(Byte isTaxExemptedSiteB) {
		this.isTaxExemptedSiteB = isTaxExemptedSiteB;
	}


	public List<NplFeasiblityBean> getFeasiblityDetails() {
		return feasiblityDetails;
	}

	public void setFeasiblityDetails(List<NplFeasiblityBean> feasiblityDetails) {
		this.feasiblityDetails = feasiblityDetails;
	}

	public List<PricingDetailBean> getPricingDetails() {
		return pricingDetails;
	}

	public void setPricingDetails(List<PricingDetailBean> pricingDetails) {
		this.pricingDetails = pricingDetails;
	}

	public Byte getIsFeasible() {
		return isFeasible;
	}

	public void setIsFeasible(Byte isFeasible) {
		this.isFeasible = isFeasible;
	}

	public Integer getLinkId() {
		return linkId;
	}

	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}

	public String getLinkCode() {
		return linkCode;
	}

	public void setLinkCode(String linkCode) {
		this.linkCode = linkCode;
	}

}
