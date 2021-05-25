package com.tcl.dias.nso.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This file contains the QuoteToLeId.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class QuoteToLeDetail {

	private Integer erfSupplierLeId;
	private Integer erfCustomerLeId;
	private String termInMonths;
	private String currency;
	private String billingAddress;
	private Integer billingAddressId;
	private String gstNumber;
	private List<Solution> solutions;

	public Integer getErfSupplierLeId() {
		return erfSupplierLeId;
	}

	public void setErfSupplierLeId(Integer erfSupplierLeId) {
		this.erfSupplierLeId = erfSupplierLeId;
	}

	public Integer getErfCustomerLeId() {
		return erfCustomerLeId;
	}

	public void setErfCustomerLeId(Integer erfCustomerLeId) {
		this.erfCustomerLeId = erfCustomerLeId;
	}

	public String getTermInMonths() {
		return termInMonths;
	}

	public void setTermInMonths(String termInMonths) {
		this.termInMonths = termInMonths;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<Solution> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<Solution> solutions) {
		this.solutions = solutions;
	}

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public Integer getBillingAddressId() {
		return billingAddressId;
	}

	public void setBillingAddressId(Integer billingAddressId) {
		this.billingAddressId = billingAddressId;
	}

	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}

	@Override
	public String toString() {
		return "QuoteToLeDetail [erfSupplierLeId=" + erfSupplierLeId + ", erfCustomerLeId=" + erfCustomerLeId
				+ ", termInMonths=" + termInMonths + ", currency=" + currency + ", billingAddress=" + billingAddress
				+ ", billingAddressId=" + billingAddressId + ", gstNumber=" + gstNumber + ", solutions=" + solutions
				+ "]";
	}

}
