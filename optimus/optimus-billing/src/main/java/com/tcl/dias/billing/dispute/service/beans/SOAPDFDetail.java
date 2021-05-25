package com.tcl.dias.billing.dispute.service.beans;

import java.math.BigDecimal;

public class SOAPDFDetail {
	
	
	public String sapcode;
	public String invoiceNum;
	public String invoiceDate;
	public String currency;
	public String invoiceAmt;
	public String dueDate;
	public String outAmt;
	public BigDecimal outAmtCalc;
	
	public BigDecimal getOutAmtCalc() {
		return outAmtCalc;
	}
	public void setOutAmtCalc(BigDecimal outamtCalc) {
		this.outAmtCalc = outamtCalc;
	}
	public String getSapcode() {
		return sapcode;
	}
	public void setSapcode(String sapcode) {
		this.sapcode = sapcode;
	}
	public String getInvoiceNum() {
		return invoiceNum;
	}
	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getInvoiceAmt() {
		return invoiceAmt;
	}
	public void setInvoiceAmt(String invoiceAmt) {
		this.invoiceAmt = invoiceAmt;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getOutAmt() {
		return outAmt;
	}
	public void setOutAmt(String outAmt) {
		this.outAmt = outAmt;
	}
	

}
