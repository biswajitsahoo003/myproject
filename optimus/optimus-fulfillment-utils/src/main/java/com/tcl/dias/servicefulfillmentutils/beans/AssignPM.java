package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class AssignPM extends BaseRequest {
	
	private String assignedPM;	
	private String comments;
	private Integer serviceId;
	private String isReassign;
	public String getAssignedPM() {
		return assignedPM;
	}
	public void setAssignedPM(String assignedPM) {
		this.assignedPM = assignedPM;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getIsReassign() {
		return isReassign;
	}
	public void setIsReassign(String isReassign) {
		this.isReassign = isReassign;
	}

}
