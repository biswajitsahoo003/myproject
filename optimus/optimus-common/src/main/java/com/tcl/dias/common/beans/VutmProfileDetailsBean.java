package com.tcl.dias.common.beans;

import java.util.List;

/**
 * 
 * This file contains the VutmProfileDetailsBean.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class VutmProfileDetailsBean {
	
	private String offeringName;
	
	private List<String> offeringDescription;

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public List<String> getOfferingDescription() {
		return offeringDescription;
	}

	public void setOfferingDescription(List<String> offeringDescription) {
		this.offeringDescription = offeringDescription;
	}
	
	

}
