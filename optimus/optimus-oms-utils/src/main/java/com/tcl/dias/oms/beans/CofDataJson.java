package com.tcl.dias.oms.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This file contains the CofDataJson.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CofDataJson {

	@JsonProperty("totalAmount")
	private CofTotalAmount totalAmount;
	@JsonProperty("solution")
	private List<ProductSolutionBean> solution;

	public CofTotalAmount getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(CofTotalAmount totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<ProductSolutionBean> getSolution() {
		return solution;
	}

	public void setSolution(List<ProductSolutionBean> solution) {
		this.solution = solution;
	}

}
