package com.tcl.dias.oms.macd.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This is the bean class for the bandwidth response
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BandwidthResponse {
	
	private String serviceId;
	
	private Byte bandwidthEdited;

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Byte getBandwidthEdited() {
		return bandwidthEdited;
	}

	public void setBandwidthEdited(Byte bandwidthEdited) {
		this.bandwidthEdited = bandwidthEdited;
	}
	
	
	
	
	

}
