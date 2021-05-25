package com.tcl.dias.common.fulfillment.beans;

/**
 * 
 * This file contains the OdrProductReferenceBean.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OdrProductReferenceBean {

	private Integer id;
	private String accessType;
	private String isActive;
	private String subVariant;
	private String variant;

	public OdrProductReferenceBean() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccessType() {
		return this.accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getSubVariant() {
		return this.subVariant;
	}

	public void setSubVariant(String subVariant) {
		this.subVariant = subVariant;
	}

	public String getVariant() {
		return this.variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

}