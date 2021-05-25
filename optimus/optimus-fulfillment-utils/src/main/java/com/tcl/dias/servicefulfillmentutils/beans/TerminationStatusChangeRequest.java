package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class TerminationStatusChangeRequest extends BaseRequest{

	private String terminationDateChange;
	private String terminateEffectiveDate;
	private String terminationHold;
	private String status;
	private String reason;
	private String etcValue;
	private String terminationSfdcOpportunityId;
	
	private List<AttachmentIdBean> documentIds;
	
	public String getTerminationDateChange() {
		return terminationDateChange;
	}
	public void setTerminationDateChange(String terminationDateChange) {
		this.terminationDateChange = terminationDateChange;
	}
	public String getTerminateEffectiveDate() {
		return terminateEffectiveDate;
	}
	public void setTerminateEffectiveDate(String terminateEffectiveDate) {
		this.terminateEffectiveDate = terminateEffectiveDate;
	}
	public String getTerminationHold() {
		return terminationHold;
	}
	public void setTerminationHold(String terminationHold) {
		this.terminationHold = terminationHold;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getEtcValue() {
		return etcValue;
	}
	public void setEtcValue(String etcValue) {
		this.etcValue = etcValue;
	}
	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}
	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
	public String getTerminationSfdcOpportunityId() {
		return terminationSfdcOpportunityId;
	}
	public void setTerminationSfdcOpportunityId(String terminationSfdcOpportunityId) {
		this.terminationSfdcOpportunityId = terminationSfdcOpportunityId;
	}
	
	
}
