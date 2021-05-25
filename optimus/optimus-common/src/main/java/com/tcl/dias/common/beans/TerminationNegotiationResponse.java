package com.tcl.dias.common.beans;

public class TerminationNegotiationResponse {

	private String serviceCode;
	private String orderCode;
	private String reason;
	
	private String negotiationResponse;
	
	private String status;
	
	private String errorMsg;
	
	private String terminationSubType;
	
	private String terminationSubReason;
	
	private String terminationReason;
	
	private String terminationRemarks;

	private String regrettedNonRegrettedTermination;

	/**
	 * @return the terminationSubType
	 */
	public String getTerminationSubType() {
		return terminationSubType;
	}

	/**
	 * @param terminationSubType the terminationSubType to set
	 */
	public void setTerminationSubType(String terminationSubType) {
		this.terminationSubType = terminationSubType;
	}

	/**
	 * @return the terminationSubReason
	 */
	public String getTerminationSubReason() {
		return terminationSubReason;
	}

	/**
	 * @param terminationSubReason the terminationSubReason to set
	 */
	public void setTerminationSubReason(String terminationSubReason) {
		this.terminationSubReason = terminationSubReason;
	}

	/**
	 * @return the terminationReason
	 */
	public String getTerminationReason() {
		return terminationReason;
	}

	/**
	 * @param terminationReason the terminationReason to set
	 */
	public void setTerminationReason(String terminationReason) {
		this.terminationReason = terminationReason;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the negotiationResponse
	 */
	public String getNegotiationResponse() {
		return negotiationResponse;
	}

	/**
	 * @param negotiationResponse the negotiationResponse to set
	 */
	public void setNegotiationResponse(String negotiationResponse) {
		this.negotiationResponse = negotiationResponse;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getTerminationRemarks() {
		return terminationRemarks;
	}

	public void setTerminationRemarks(String terminationRemarks) {
		this.terminationRemarks = terminationRemarks;
	}

	public String getRegrettedNonRegrettedTermination() {
		return regrettedNonRegrettedTermination;
	}

	public void setRegrettedNonRegrettedTermination(String regrettedNonRegrettedTermination) {
		this.regrettedNonRegrettedTermination = regrettedNonRegrettedTermination;
	}



}
