package com.tcl.dias.common.beans;

import java.io.Serializable;

public class UserGroupToCustomerLeBean implements Serializable {

	private Integer id;

	private Integer erfCusCustomerId;

	private Integer erfCusCustomerLeId;

	private Byte isActive;
	
	private String erfCusCustomerName;
	
	private String erfCusCustomerLeName;
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the erfCusCustomerId
	 */
	public Integer getErfCusCustomerId() {
		return erfCusCustomerId;
	}

	/**
	 * @param erfCusCustomerId
	 *            the erfCusCustomerId to set
	 */
	public void setErfCusCustomerId(Integer erfCusCustomerId) {
		this.erfCusCustomerId = erfCusCustomerId;
	}

	/**
	 * @return the erfCusCustomerLeId
	 */
	public Integer getErfCusCustomerLeId() {
		return erfCusCustomerLeId;
	}

	/**
	 * @param erfCusCustomerLeId
	 *            the erfCusCustomerLeId to set
	 */
	public void setErfCusCustomerLeId(Integer erfCusCustomerLeId) {
		this.erfCusCustomerLeId = erfCusCustomerLeId;
	}

	/**
	 * @return the isActive
	 */
	public Byte getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setIsActive(Byte isActive) {
		this.isActive = isActive;
	}

	public String getErfCusCustomerName() {
		return erfCusCustomerName;
	}

	public void setErfCusCustomerName(String erfCusCustomerName) {
		this.erfCusCustomerName = erfCusCustomerName;
	}

	public String getErfCusCustomerLeName() {
		return erfCusCustomerLeName;
	}

	public void setErfCusCustomerLeName(String erfCusCustomerLeName) {
		this.erfCusCustomerLeName = erfCusCustomerLeName;
	}
	
	
}
