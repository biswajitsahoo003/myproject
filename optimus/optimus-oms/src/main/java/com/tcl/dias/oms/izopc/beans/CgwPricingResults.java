package com.tcl.dias.oms.izopc.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * This file contains the CgwPricingResults.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "quote_id",
	"currency",
	"node_details",
	"gateway_total_contract_value",
	"version"})
public class CgwPricingResults {
	@JsonProperty("quote_id")
	private String quoteId;
	@JsonProperty("currency")
	private String currency;
	@JsonProperty("node_details")
	private List<NodePricingDetails> nodePricingDetails;
	@JsonProperty("gateway_total_contract_value")
	private Double gatewayTcv;
	@JsonProperty("version")
	private String version;
	
	public String getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public List<NodePricingDetails> getNodePricingDetails() {
		return nodePricingDetails;
	}
	public void setNodePricingDetails(List<NodePricingDetails> nodePricingDetails) {
		this.nodePricingDetails = nodePricingDetails;
	}
	public Double getGatewayTcv() {
		return gatewayTcv;
	}
	public void setGatewayTcv(Double gatewayTcv) {
		this.gatewayTcv = gatewayTcv;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

}
