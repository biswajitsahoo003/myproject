package com.tcl.dias.common.redis.beans;

import java.io.Serializable;

/**
 * Bean class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CustomerDetail implements Serializable {

	private static final long serialVersionUID = 421163718445776624L;

	private Integer customerId;

	private Integer customerLeId;

	private String customerAcId;

	private String customerCode;

	private String customerEmailId;

	private String customerName;

	private Integer erfCustomerId;

	private Byte status;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerAcId() {
		return customerAcId;
	}

	public void setCustomerAcId(String customerAcId) {
		this.customerAcId = customerAcId;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerEmailId() {
		return customerEmailId;
	}

	public void setCustomerEmailId(String customerEmailId) {
		this.customerEmailId = customerEmailId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getErfCustomerId() {
		return erfCustomerId;
	}

	public void setErfCustomerId(Integer erfCustomerId) {
		this.erfCustomerId = erfCustomerId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Integer getCustomerLeId() {
		return customerLeId;
	}

	public void setCustomerLeId(Integer customerLeId) {
		this.customerLeId = customerLeId;
	}

	@Override
	public String toString() {
		return "CustomerDetail{" +
				"customerId=" + customerId +
				", customerLeId=" + customerLeId +
				", customerAcId='" + customerAcId + '\'' +
				", customerCode='" + customerCode + '\'' +
				", customerEmailId='" + customerEmailId + '\'' +
				", customerName='" + customerName + '\'' +
				", erfCustomerId=" + erfCustomerId +
				", status=" + status +
				'}';
	}
}
