package com.tcl.dias.billing.dispute.service.beans;

import java.math.BigDecimal;

public class BalanceSheet {
	
	public String currency;

	public String cocd;


	public String sapcode;

	public String name;

	public BigDecimal collection;
	public BigDecimal billing;
	
	public BigDecimal tdsProvisions;
	public BigDecimal tdsCertReceived;
	public BigDecimal creditNotes;

	
	public BigDecimal getCreditNotes() {
		return creditNotes;
	}

	public void setCreditNotes(BigDecimal creditNotes) {
		this.creditNotes = creditNotes;
	}

	public String advance;
	public String getAdvance() {
		return advance;
	}

	public void setAdvance(String advance) {
		this.advance = advance;
	}

	public String getSecurityDeposit() {
		return securityDeposit;
	}

	public void setSecurityDeposit(String securityDeposit) {
		this.securityDeposit = securityDeposit;
	}

	public BigDecimal getClosingBal() {
		return closingBal;
	}

	public void setClosingBal(BigDecimal closingBal) {
		this.closingBal = closingBal;
	}

	public String securityDeposit;
	public BigDecimal closingBal;


	public BigDecimal getTdsProvisions() {
		return tdsProvisions;
	}

	public void setTdsProvisions(BigDecimal tdsProvisions) {
		this.tdsProvisions = tdsProvisions;
	}

	public BigDecimal getCollection() {
		return collection;
	}

	public void setCollection(BigDecimal collection) {
		this.collection = collection;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getSapcode() {
		return sapcode;
	}

	public void setSapcode(String sapCode) {
		this.sapcode = sapCode;
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

	public String getCocd() {
		return cocd;
	}

	public void setCocd(String cocd) {
		this.cocd = cocd;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}


}
