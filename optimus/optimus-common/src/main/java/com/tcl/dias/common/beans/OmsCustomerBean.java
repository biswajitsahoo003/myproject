package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Bean class for Customer used in Oms Customer Listener
 *
 * @author SuruchiA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class OmsCustomerBean {

	private Integer customerId;
	private String customerName;
	private Integer partnerId;
	private Integer erfCusId;
	private String customerCode;
	private String customerEmail;
	private Integer status;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public Integer getErfCusId() {
		return erfCusId;
	}

	public void setErfCusId(Integer erfCusId) {
		this.erfCusId = erfCusId;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CustomerDetailBean1 [customerId=" + customerId + ", customerName=" + customerName + ", partnerId="
				+ partnerId + ", erfCusId=" + erfCusId + ", customerCode=" + customerCode + ", customerEmail="
				+ customerEmail + ", status=" + status + ", getCustomerId()=" + getCustomerId() + ", getCustomerName()="
				+ getCustomerName() + ", getPartnerId()=" + getPartnerId() + ", getErfCusId()=" + getErfCusId()
				+ ", getCustomerCode()=" + getCustomerCode() + ", getCustomerEmail()=" + getCustomerEmail()
				+ ", getStatus()=" + getStatus() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

}
