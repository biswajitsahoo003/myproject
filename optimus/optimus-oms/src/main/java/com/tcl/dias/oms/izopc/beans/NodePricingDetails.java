package com.tcl.dias.oms.izopc.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * This file contains the NodePricingDetails.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "node",
	"gateway_mrc",
	"gateway_arc"})
public class NodePricingDetails {
	@JsonProperty("node")
	private String node;
	@JsonProperty("gateway_mrc")
	private Double gatewayMrc;
	@JsonProperty("gateway_arc")
	private Double gatewayArc;
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public Double getGatewayMrc() {
		return gatewayMrc;
	}
	public void setGatewayMrc(Double gatewayMrc) {
		this.gatewayMrc = gatewayMrc;
	}
	public Double getGatewayArc() {
		return gatewayArc;
	}
	public void setGatewayArc(Double gatewayArc) {
		this.gatewayArc = gatewayArc;
	}

}
