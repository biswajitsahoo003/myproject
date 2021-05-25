package com.tcl.dias.oms.gde.beans;

/**
 * This file contains the FeasibilityRequestBean.java class for GDE feasibility request
 * 
 *
 * @author archchan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class FeasibilityRequestBean {

	private Integer legalEntityId;
	
	private String legalEntityName;

	private Integer siteId;
	
	private Integer linkId;

	public Integer getLegalEntityId() {
		return legalEntityId;
	}

	public void setLegalEntityId(Integer legalEntityId) {
		this.legalEntityId = legalEntityId;
	}

	public String getLegalEntityName() {
		return legalEntityName;
	}

	public void setLegalEntityName(String legalEntityName) {
		this.legalEntityName = legalEntityName;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getLinkId() {
		return linkId;
	}

	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}
	
	
}
