package com.tcl.dias.common.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This class is used as a response bean in credit check
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductServicesRecord {
	
	private String opportunityNameC;
	
	private String Id;
	
	private String productMRCc;
	
	private String productNRCc;

	/**
	 * @return the opportunityNameC
	 */
	public String getOpportunityNameC() {
		return opportunityNameC;
	}

	/**
	 * @param opportunityNameC the opportunityNameC to set
	 */
	public void setOpportunityNameC(String opportunityNameC) {
		this.opportunityNameC = opportunityNameC;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return Id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		Id = id;
	}

	/**
	 * @return the productMRCc
	 */
	public String getProductMRCc() {
		return productMRCc;
	}

	/**
	 * @param productMRCc the productMRCc to set
	 */
	public void setProductMRCc(String productMRCc) {
		this.productMRCc = productMRCc;
	}

	/**
	 * @return the productNRCc
	 */
	public String getProductNRCc() {
		return productNRCc;
	}

	/**
	 * @param productNRCc the productNRCc to set
	 */
	public void setProductNRCc(String productNRCc) {
		this.productNRCc = productNRCc;
	}

	@Override
	public String toString() {
		return "ProductServicesRecord [opportunityNameC=" + opportunityNameC + ", Id=" + Id + ", productMRCc="
				+ productMRCc + ", productNRCc=" + productNRCc + "]";
	}
	
	

}
