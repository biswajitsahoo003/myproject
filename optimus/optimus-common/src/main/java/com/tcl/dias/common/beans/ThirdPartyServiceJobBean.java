package com.tcl.dias.common.beans;

public class ThirdPartyServiceJobBean {

	private Integer id;
	private Integer seqNum;
	private String serviceType;
	private String serviceStatus;
	private String requestPayload;
	private String reasonForMakingSuccess;
	private Boolean makeSuccess;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	public String getRequestPayload() {
		return requestPayload;
	}
	public void setRequestPayload(String requestPayload) {
		this.requestPayload = requestPayload;
	}
	public String getReasonForMakingSuccess() {
		return reasonForMakingSuccess;
	}
	public void setReasonForMakingSuccess(String reasonForMakingSuccess) {
		this.reasonForMakingSuccess = reasonForMakingSuccess;
	}
	public Boolean getMakeSuccess() {
		return makeSuccess;
	}
	public void setMakeSuccess(Boolean makeSuccess) {
		this.makeSuccess = makeSuccess;
	}
	
	
	
}

