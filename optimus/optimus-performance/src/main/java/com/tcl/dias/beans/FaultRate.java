package com.tcl.dias.beans;

/**
 * This file contains the severity impact and the corresponding count against a
 * month or a product.
 * 
 * @author KRUTSRIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class FaultRate {

	private String productOrMonthWise;

	private CircuitDetails ciruitDetail;

	public CircuitDetails getCiruitDetail() {
		return ciruitDetail;
	}

	public void setCiruitDetail(CircuitDetails ciruitDetail) {
		this.ciruitDetail = ciruitDetail;
	}

	public String getProductOrMonthWise() {
		return productOrMonthWise;
	}

	public void setProductOrMonthWise(String productOrMonthWise) {
		this.productOrMonthWise = productOrMonthWise;
	}

}
