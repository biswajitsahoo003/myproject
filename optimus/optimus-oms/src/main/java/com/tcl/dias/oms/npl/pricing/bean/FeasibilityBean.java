package com.tcl.dias.oms.npl.pricing.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * This file contains the FeasibilityBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class FeasibilityBean {

	private Integer legalEntityId;
	
	private String legalEntityName;

	private List<FeasibilitySiteEngineBean> sites;

	/**
	 * @return the sites
	 */
	public List<FeasibilitySiteEngineBean> getSites() {

		if (sites == null) {
			sites = new ArrayList<>();
		}
		return sites;
	}

	/**
	 * @param sites
	 *            the sites to set
	 */
	public void setSites(List<FeasibilitySiteEngineBean> sites) {
		this.sites = sites;
	}

	/**
	 * @return the legalEntityId
	 */
	public Integer getLegalEntityId() {
		return legalEntityId;
	}

	/**
	 * @param legalEntityId
	 *            the legalEntityId to set
	 */
	public void setLegalEntityId(Integer legalEntityId) {
		this.legalEntityId = legalEntityId;
	}

	/**
	 * @return the legalEntityName
	 */
	public String getLegalEntityName() {
		return legalEntityName;
	}

	/**
	 * @param legalEntityName the legalEntityName to set
	 */
	public void setLegalEntityName(String legalEntityName) {
		this.legalEntityName = legalEntityName;
	}

	
	
}
