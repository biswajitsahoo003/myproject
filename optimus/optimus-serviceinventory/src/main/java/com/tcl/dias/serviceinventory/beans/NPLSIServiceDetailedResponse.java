package com.tcl.dias.serviceinventory.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This file contains the ServiceInventory details for NPL product only.
 * 
 *
 * @author DSIVALIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class NPLSIServiceDetailedResponse {

    private String productName;

	private List<NPLSISolutionDataOffering> siInfo;


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
	 * @return the siInfo
	 */
	public List<NPLSISolutionDataOffering> getSiInfo() {
		return siInfo;
	}

	/**
	 * @param siInfo the siInfo to set
	 */
	public void setSiInfo(List<NPLSISolutionDataOffering> siInfo) {
		this.siInfo = siInfo;
	}
	
}
