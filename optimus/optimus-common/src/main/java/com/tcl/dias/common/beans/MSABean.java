package com.tcl.dias.common.beans;
/**
 * This class contains the MSA information
 * 
 * @author Biswajit
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

import java.io.Serializable;

public class MSABean implements Serializable{
	
	private String productName;
	
	private Boolean isMSAUploaded;
	
	private String displayName;
	
	private String name;
	
	private Integer customerLeId;

	public Integer getCustomerLeId() {
		return customerLeId;
	}

	public void setCustomerLeId(Integer customerLeId) {
		this.customerLeId = customerLeId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDisplayName() {
		return displayName;
	}
	public Boolean getIsMSAUploaded() {
		return isMSAUploaded;
	}

	public void setIsMSAUploaded(Boolean isMSAUploaded) {
		this.isMSAUploaded = isMSAUploaded;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
