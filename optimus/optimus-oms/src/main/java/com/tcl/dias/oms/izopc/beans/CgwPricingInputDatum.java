package com.tcl.dias.oms.izopc.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the CgwPricingInputDatum.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "quote_id",
	"opportunity_term",
	"node",
	"sdwan_components",
	"agr_bw_mbps"})
public class CgwPricingInputDatum {
	@JsonProperty("quote_id")
	private String quoteId;
	@JsonProperty("opportunity_term")
	private Integer opportunityTerm;
	@JsonProperty("node")
	private String node;
	@JsonProperty("sdwan_components")
	private String sdwanComponents;
	@JsonProperty("agr_bw_mbps")
	private Integer agrBwMbps;
	public String getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}
	public Integer getOpportunityTerm() {
		return opportunityTerm;
	}
	public void setOpportunityTerm(Integer opportunityTerm) {
		this.opportunityTerm = opportunityTerm;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getSdwanComponents() {
		return sdwanComponents;
	}
	public void setSdwanComponents(String sdwanComponents) {
		this.sdwanComponents = sdwanComponents;
	}
	public Integer getAgrBwMbps() {
		return agrBwMbps;
	}
	public void setAgrBwMbps(Integer agrBwMbps) {
		this.agrBwMbps = agrBwMbps;
	}
	
	
}
