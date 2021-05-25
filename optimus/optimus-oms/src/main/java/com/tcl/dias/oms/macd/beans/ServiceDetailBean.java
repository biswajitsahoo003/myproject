package com.tcl.dias.oms.macd.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This is the bean class for comparing macd attributes
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceDetailBean {

	private Integer downstreamOrderId;

	private Integer serviceDetailId;

	private String serviceId;

	public Integer getDownstreamOrderId() {
		return downstreamOrderId;
	}

	public void setDownstreamOrderId(Integer downstreamOrderId) {
		this.downstreamOrderId = downstreamOrderId;
	}

	public Integer getServiceDetailId() {
		return serviceDetailId;
	}

	public void setServiceDetailId(Integer serviceDetailId) {
		this.serviceDetailId = serviceDetailId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	
}
