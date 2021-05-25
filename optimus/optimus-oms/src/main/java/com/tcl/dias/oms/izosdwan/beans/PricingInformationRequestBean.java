package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * This file contains the request to get the pricing information
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class PricingInformationRequestBean implements Serializable{
	private Integer quoteId;
	private String productName;
	private List<Integer> siteIds;
	private String siteType;
	public Integer getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public List<Integer> getSiteIds() {
		return siteIds;
	}
	public void setSiteIds(List<Integer> siteIds) {
		this.siteIds = siteIds;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	
}
