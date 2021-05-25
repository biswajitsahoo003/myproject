package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class TerminationSalesnegotiationBean extends BaseRequest {
	
	private String negotiationOutput;
	private String reason;
	
	private String terminationSubType;
	
	private String terminationSubReason;
	
	private String terminationReason;
	
	private String terminationRemark;
	
	private String regrettedNonRegrettedTermination;
	
	private List<AttachmentIdBean> documentIds;
	
	
	
	
	
	
	
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getNegotiationOutput() {
		return negotiationOutput;
	}

	public void setNegotiationOutput(String negotiationOutput) {
		this.negotiationOutput = negotiationOutput;
	}

	public String getTerminationRemark() {
		return terminationRemark;
	}

	public void setTerminationRemarks(String terminationRemark) {
		this.terminationRemark = terminationRemark;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getRegrettedNonRegrettedTermination() {
		return regrettedNonRegrettedTermination;
	}

	public void setRegrettedNonRegrettedTermination(String regrettedNonRegrettedTermination) {
		this.regrettedNonRegrettedTermination = regrettedNonRegrettedTermination;
	}
	
	
	
	
	
	

}
