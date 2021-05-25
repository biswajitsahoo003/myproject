package com.tcl.dias.serviceinventory.beans;

/**
 * This file contains the SIDetailedInfoResponse.java class.
 *
 *
 * @author DSIVALIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SIDetailedInfoResponse {

	private String serviceDetailId;

	private String productName;

	private SISolutionOffering solutions;

	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return serviceDetailId;
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(String serviceDetailId) {
		this.serviceDetailId = serviceDetailId;
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * @return the solutions
	 */
	public SISolutionOffering getSolutions() {
		return solutions;
	}

	/**
	 * @param solutions the solutions to set
	 */
	public void setSolutions(SISolutionOffering solutions) {
		this.solutions = solutions;
	}

}

