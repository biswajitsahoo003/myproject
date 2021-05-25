package com.tcl.dias.common.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * This file contains the ProductSlaBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ProductSlaBean {

	private Integer tier;
	private String tierName;
	private List<SLaDetailsBean> sLaDetails;

	/**
	 * @return the sLaDetails
	 */
	public List<SLaDetailsBean> getsLaDetails() {

		if (sLaDetails == null) {
			sLaDetails = new ArrayList<>();
		}
		return sLaDetails;
	}

	/**
	 * @param sLaDetails
	 *            the sLaDetails to set
	 */
	public void setsLaDetails(List<SLaDetailsBean> sLaDetails) {
		this.sLaDetails = sLaDetails;
	}

	public Integer getTier() {
		return tier;
	}

	public void setTier(Integer tier) {
		this.tier = tier;
	}

	public String getTierName() {
		return tierName;
	}

	public void setTierName(String tierName) {
		this.tierName = tierName;
	}

}
