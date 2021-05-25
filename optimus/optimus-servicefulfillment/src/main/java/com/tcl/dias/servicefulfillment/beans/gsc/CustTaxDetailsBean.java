package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustTaxDetailsBean {

	private List<String> accountNumberType;
	private List<String> taxNumber;
	private List<String> printTaxFlag;
	private List<String> vatExemptionReason;
	public List<String> getAccountNumberType() {
		return accountNumberType;
	}
	public void setAccountNumberType(List<String> accountNumberType) {
		this.accountNumberType = accountNumberType;
	}
	public List<String> getTaxNumber() {
		return taxNumber;
	}
	public void setTaxNumber(List<String> taxNumber) {
		this.taxNumber = taxNumber;
	}
	public List<String> getPrintTaxFlag() {
		return printTaxFlag;
	}
	public void setPrintTaxFlag(List<String> printTaxFlag) {
		this.printTaxFlag = printTaxFlag;
	}
	public List<String> getVatExemptionReason() {
		return vatExemptionReason;
	}
	public void setVatExemptionReason(List<String> vatExemptionReason) {
		this.vatExemptionReason = vatExemptionReason;
	}
	
	
}
