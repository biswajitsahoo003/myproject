package com.tcl.dias.billing.dispute.service.beans;
import java.math.BigDecimal;
import java.util.List;

public class SoaPDFBean {
	
	
public List<SOAPDFDetail> invoices;

public List<SOAPDFDetail> payments;

public List<SOAPDFDetail> creditNotes;

public List<SOAPDFDetail> tdsProvisions;

public List<SOAPDFDetail> tdsCertReceivedList;

public List<SOAPDFDetail> getTdsCertReceivedList() {
	return tdsCertReceivedList;
}

public void setTdsCertReceivedList(List<SOAPDFDetail> tdsCertReceivedList) {
	this.tdsCertReceivedList = tdsCertReceivedList;
}

public List<BalanceSheet> balSheet;

public BigDecimal collection;

public BigDecimal billing;

public BigDecimal tdsCertReceived;



public String sapcode;

public String statementDate;



public String getStatementDate() {
	return statementDate;
}

public void setStatementDate(String statementDate) {
	this.statementDate = statementDate;
}

public String getSapcode() {
	return sapcode;
}

public void setSapcode(String sapcode) {
	this.sapcode = sapcode;
}

public BigDecimal getTdsCertReceived() {
	return tdsCertReceived;
}

public void setTdsCertReceived(BigDecimal tdsCertReceived) {
	this.tdsCertReceived = tdsCertReceived;
}



public BigDecimal getBilling() {
	return billing;
}

public void setBilling(BigDecimal billing) {
	this.billing = billing;
}

public BigDecimal getCollection() {
	return collection;
}

public void setCollection(BigDecimal collection) {
	this.collection = collection;
}

public String name;
public String cocd;


public String getCocd() {
	return cocd;
}

public void setCocd(String cocd) {
	this.cocd = cocd;
}

public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}


public List<BalanceSheet> getBalSheet() {
	return balSheet;
}

public void setBalSheet(List<BalanceSheet> balSheet) {
	this.balSheet = balSheet;
}

public List<SOAPDFDetail> getTdsProvisions() {
	return tdsProvisions;
}

public void setTdsProvisions(List<SOAPDFDetail> tdsProvisions) {
	this.tdsProvisions = tdsProvisions;
}

public List<SOAPDFDetail> getCreditNotes() {
	return creditNotes;
}

public void setCreditNotes(List<SOAPDFDetail> creditNotes) {
	this.creditNotes = creditNotes;
}

public List<SOAPDFDetail> getPayments() {
	return payments;
}

public void setPayments(List<SOAPDFDetail> payments) {
	this.payments = payments;
}

public List<SOAPDFDetail> getInvoices() {
	return invoices;
}

public void setInvoices(List<SOAPDFDetail> invoices) {
	this.invoices = invoices;
}





}