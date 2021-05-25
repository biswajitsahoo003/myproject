package com.tcl.dias.oms.gsc.tiger.beans;

public class DidDetail {
	private Integer didDetailsId;
	private String didNumber;
	private Boolean portingRequired;
	private String connectionDtgHeader;

	public Integer getDidDetailsId() {
		return didDetailsId;
	}

	public void setDidDetailsId(Integer didDetailsId) {
		this.didDetailsId = didDetailsId;
	}

	public String getDidNumber() {
		return didNumber;
	}

	public void setDidNumber(String didNumber) {
		this.didNumber = didNumber;
	}

	public Boolean getPortingRequired() {
		return portingRequired;
	}

	public void setPortingRequired(Boolean portingRequired) {
		this.portingRequired = portingRequired;
	}

	public String getConnectionDtgHeader() {
		return connectionDtgHeader;
	}

	public void setConnectionDtgHeader(String connectionDtgHeader) {
		this.connectionDtgHeader = connectionDtgHeader;
	}
}
