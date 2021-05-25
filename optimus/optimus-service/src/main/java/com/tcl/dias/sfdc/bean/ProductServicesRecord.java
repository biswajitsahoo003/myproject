package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This class is used as a response bean in credit check
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "Opportunity_Name__c","Id","Product_MRC__c","Product_NRC__c"})
public class ProductServicesRecord {
	
	@JsonProperty("Opportunity_Name__c")
	private String opportunityNameC;
	
	@JsonProperty("Id")
	private String Id;
	
	@JsonProperty("Product_MRC__c")
	private String productMRCc;
	
	@JsonProperty("Product_NRC__c")
	private String productNRCc;

	/**
	 * @return the opportunityNameC
	 */
	@JsonProperty("Opportunity_Name__c")
	public String getOpportunityNameC() {
		return opportunityNameC;
	}

	/**
	 * @param opportunityNameC the opportunityNameC to set
	 */
	@JsonProperty("Opportunity_Name__c")
	public void setOpportunityNameC(String opportunityNameC) {
		this.opportunityNameC = opportunityNameC;
	}

	/**
	 * @return the id
	 */
	@JsonProperty("Id")
	public String getId() {
		return Id;
	}

	/**
	 * @param id the id to set
	 */
	@JsonProperty("Id")
	public void setId(String id) {
		Id = id;
	}

	/**
	 * @return the productMRCc
	 */
	@JsonProperty("Product_MRC__c")
	public String getProductMRCc() {
		return productMRCc;
	}

	/**
	 * @param productMRCc the productMRCc to set
	 */
	@JsonProperty("Product_MRC__c")
	public void setProductMRCc(String productMRCc) {
		this.productMRCc = productMRCc;
	}

	/**
	 * @return the productNRCc
	 */
	@JsonProperty("Product_NRC__c")
	public String getProductNRCc() {
		return productNRCc;
	}

	/**
	 * @param productNRCc the productNRCc to set
	 */
	@JsonProperty("Product_NRC__c")
	public void setProductNRCc(String productNRCc) {
		this.productNRCc = productNRCc;
	}

	

}
