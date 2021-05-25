package com.tcl.dias.common.beans;

import java.io.Serializable;

/**
 * DopEmail Request bean for DOP matrix email
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SignDetailBean implements Serializable
{
	private String accountManagerName;
	private Double orderValue;
	private String customerSegment;

	public String getAccountManagerName() {
		return accountManagerName;
	}

	public void setAccountManagerName(String accountManagerName) {
		this.accountManagerName = accountManagerName;
	}
	public Double getOrderValue() {
		return orderValue;
	}

	public void setOrderValue(Double orderValue) {
		this.orderValue = orderValue;
	}

	public String getCustomerSegment() {
		return customerSegment;
	}

	public void setCustomerSegment(String customerSegment) {
		this.customerSegment = customerSegment;
	}

	@Override
	public String toString() {
		return "SignDetailBean{" +
				"accountManagerName='" + accountManagerName + '\'' +
				", orderValue=" + orderValue +
				", customerSegment='" + customerSegment + '\'' +
				'}';
	}
}
