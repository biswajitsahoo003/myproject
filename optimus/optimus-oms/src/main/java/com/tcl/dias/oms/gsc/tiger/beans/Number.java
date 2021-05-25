package com.tcl.dias.oms.gsc.tiger.beans;

import java.util.List;

public class Number {
	private String numberDetailsId;
	private String accessNumber;
	private Boolean portingRequired;
	private String ituRegNo;
	private List<CallType> callType;
	private List<TerminationDetail> terminationDetails;

	public String getNumberDetailsId() {
		return numberDetailsId;
	}

	public void setNumberDetailsId(String numberDetailsId) {
		this.numberDetailsId = numberDetailsId;
	}

	public String getAccessNumber() {
		return accessNumber;
	}

	public void setAccessNumber(String accessNumber) {
		this.accessNumber = accessNumber;
	}

	public Boolean getPortingRequired() {
		return portingRequired;
	}

	public void setPortingRequired(Boolean portingRequired) {
		this.portingRequired = portingRequired;
	}

	public String getItuRegNo() {
		return ituRegNo;
	}

	public void setItuRegNo(String ituRegNo) {
		this.ituRegNo = ituRegNo;
	}

	public List<CallType> getCallType() {
		return callType;
	}

	public void setCallType(List<CallType> callType) {
		this.callType = callType;
	}

	public List<TerminationDetail> getTerminationDetails() {
		return terminationDetails;
	}

	public void setTerminationDetails(List<TerminationDetail> terminationDetails) {
		this.terminationDetails = terminationDetails;
	}
}
