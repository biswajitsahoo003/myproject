package com.tcl.dias.serviceinventory.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This file contains the ServiceInventory details for NPL product only.
 * 
 *
 * @author archchan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GdeSIServiceDetailedResponse {

    private String productName;

	private List<GdeSISolutionDataOffering> siInfo;


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
	public List<GdeSISolutionDataOffering> getSiInfo() {
		return siInfo;
	}

	/**
	 * @param siInfo the siInfo to set
	 */
	public void setSiInfo(List<GdeSISolutionDataOffering> siInfo) {
		this.siInfo = siInfo;
	}
	
}
