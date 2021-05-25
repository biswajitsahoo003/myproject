package com.tcl.dias.oms.gde.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * class for GdePricingFeasibility response
 * @author archchan
 *
 */
@JsonInclude(Include.NON_NULL)
public class GdePricingFeasibilityBean implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer linkId;

	private String linkCode;

	private Byte isFeasible;
	
    private Byte isTaxExemptedSiteA;
	
	private Byte isTaxExemptedSiteB;

	private List<GdeFeasiblityBean> feasiblityDetails;
	
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


	public List<GdeFeasiblityBean> getFeasiblityDetails() {
		return feasiblityDetails;
	}

	public void setFeasiblityDetails(List<GdeFeasiblityBean> feasiblityDetails) {
		this.feasiblityDetails = feasiblityDetails;
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
