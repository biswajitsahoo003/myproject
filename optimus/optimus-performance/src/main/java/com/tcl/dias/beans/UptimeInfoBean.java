package com.tcl.dias.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This file contains the circuit details - product wise for Uptime related data.
 * 
 *
 * @author Deepika Sivalingam.
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class UptimeInfoBean {

	private String product;
	
	private CircuitInfoBean circuitInfo;

	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * @return the circuitInfo
	 */
	public CircuitInfoBean getCircuitInfo() {
		return circuitInfo;
	}

	/**
	 * @param circuitInfo the circuitInfo to set
	 */
	public void setCircuitInfo(CircuitInfoBean circuitInfo) {
		this.circuitInfo = circuitInfo;
	}
	
}
