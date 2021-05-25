package com.tcl.dias.common.beans;

public class LeSapCodeBean {

	private Integer id;

	private Integer customerLeId;

	private String codeValue;

	private String codeType;

	private String isActive;

	private String customerLeName;

	private String sapCodeValue;

	public LeSapCodeBean() {
	}

	public LeSapCodeBean(String customerLeName, String sapCodeValue) {
		this.customerLeName = customerLeName;
		this.sapCodeValue = sapCodeValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCustomerLeId() {
		return customerLeId;
	}

	public void setCustomerLeId(Integer customerLeId) {
		this.customerLeId = customerLeId;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getCustomerLeName() {
		return customerLeName;
	}

	public void setCustomerLeName(String customerLeName) {
		this.customerLeName = customerLeName;
	}

	public String getSapCodeValue() {
		return sapCodeValue;
	}

	public void setSapCodeValue(String sapCodeValue) {
		this.sapCodeValue = sapCodeValue;
	}

	@Override
	public String toString() {
		return "LeSapCodeBean{" +
				"id=" + id +
				", customerLeId=" + customerLeId +
				", codeValue='" + codeValue + '\'' +
				", codeType='" + codeType + '\'' +
				", isActive='" + isActive + '\'' +
				", customerLeName='" + customerLeName + '\'' +
				", sapCodeValue='" + sapCodeValue + '\'' +
				'}';
	}
}
