package com.tcl.dias.common.sfdc.response.bean;

/**
 * 
 * @author vpachava
 *
 */
public class BundledOpportunityResponseBean {
	
	private String status;
	
	private String serviceSegment;
	
	private BundledOpportunityResponse 	opportunity;
	
	private String message;
	
	private String customOptyId;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getServiceSegment() {
		return serviceSegment;
	}

	public void setServiceSegment(String serviceSegment) {
		this.serviceSegment = serviceSegment;
	}

	public BundledOpportunityResponse getOpportunity() {
		return opportunity;
	}

	public void setOpportunity(BundledOpportunityResponse opportunity) {
		this.opportunity = opportunity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCustomOptyId() {
		return customOptyId;
	}

	public void setCustomOptyId(String customOptyId) {
		this.customOptyId = customOptyId;
	}

	@Override
	public String toString() {
		return "BundledOpportunityResponseBean [status=" + status + ", serviceSegment=" + serviceSegment
				+ ", opportunity=" + opportunity + ", message=" + message + ", customOptyId=" + customOptyId + "]";
	}
	
	

}
