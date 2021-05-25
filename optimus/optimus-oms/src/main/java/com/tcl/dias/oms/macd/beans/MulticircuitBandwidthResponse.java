package com.tcl.dias.oms.macd.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This is the bean class for the multicircuit bandwidth response
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MulticircuitBandwidthResponse {

	private List<BandwidthResponse> bandwidthResponseList;

	public List<BandwidthResponse> getBandwidthResponseList() {
		return bandwidthResponseList;
	}

	public void setBandwidthResponseList(List<BandwidthResponse> bandwidthResponseList) {
		this.bandwidthResponseList = bandwidthResponseList;
	}
	
	
}
