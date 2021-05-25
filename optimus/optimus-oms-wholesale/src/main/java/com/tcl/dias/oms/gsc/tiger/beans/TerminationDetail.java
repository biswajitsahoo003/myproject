package com.tcl.dias.oms.gsc.tiger.beans;

public class TerminationDetail {
	private String termDetailsId;
	private String countryAbbr;
	private String existingConnectionID;
	private Boolean newConnectionRequired;

	public String getTermDetailsId() {
		return termDetailsId;
	}

	public void setTermDetailsId(String termDetailsId) {
		this.termDetailsId = termDetailsId;
	}

	public String getCountryAbbr() {
		return countryAbbr;
	}

	public void setCountryAbbr(String countryAbbr) {
		this.countryAbbr = countryAbbr;
	}

	public String getExistingConnectionID() {
		return existingConnectionID;
	}

	public void setExistingConnectionID(String existingConnectionID) {
		this.existingConnectionID = existingConnectionID;
	}

	public Boolean getNewConnectionRequired() {
		return newConnectionRequired;
	}

	public void setNewConnectionRequired(Boolean newConnectionRequired) {
		this.newConnectionRequired = newConnectionRequired;
	}
}
